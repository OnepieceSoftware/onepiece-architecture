package software.onepiece.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GradleVersion
import software.onepiece.extension.SoftwareArchModel

abstract class UpdateArch : DefaultTask() {

    @get:Internal
    abstract val model: Property<SoftwareArchModel>

    @get:Internal
    abstract val destination: DirectoryProperty

    private fun Directory.addEmptyBuildFile(name: String = "build") {
        asFile.mkdirs()
        file("${name}.gradle.kts").asFile.writeText("plugins {\n}\n\ngroup = \"\"\n\ndependencies {\n}\n")
    }

    private fun Directory.updateBuildFile(plugins: List<String>, group: String, dependencies: List<String> = emptyList(), name: String = "build") {
        val buildFile = file("${name}.gradle.kts")
        if (!buildFile.asFile.exists()) {
            addEmptyBuildFile(name)
        }

        buildFile.asFile.writeText(buildFile.asFile.readText()
            .replace("""plugins \{[^}]*}""".toRegex(), "plugins {${plugins.joinToString("") { "\n    ${if(it == "kotlin-dsl") "`kotlin-dsl`" else "id(\"$it\")"}"}}\n}")
            .replace("""group = ".*"""".toRegex(), """group = "\$group"""")
            .replace("""dependencies \{[^}]*}""".toRegex(), "dependencies {${dependencies.joinToString("") { "\n    implementation($it)"}}\n}")

        )
    }

    private fun Directory.addBuildLogicSettingsFile(subprojects: List<String>) {
        asFile.mkdirs()
        val settingsFile = file("settings.gradle.kts")

        settingsFile.asFile.writeText(
            """dependencyResolutionManagement {      
    repositories {
        gradlePluginPortal()
    }
}

${subprojects.joinToString("\n") { "include(\"$it\")" } }
""")
    }

    private fun Directory.addComponentSettingsFile(componentDependencies: List<String>, subprojects: List<String>) {
        asFile.mkdirs()
        val settingsFile = file("settings.gradle.kts")

        settingsFile.asFile.writeText(
            """pluginManagement {
    includeBuild("../build-logic")
}
dependencyResolutionManagement {      
    repositories {
        mavenCentral()
    }
}

${componentDependencies.joinToString("\n") { "includeBuild(\"../$it\")" } }

${subprojects.joinToString("\n") { "include(\"$it\")" } }
""")
    }

    private fun Directory.addGradleWrapper() {
        val gradleVersion = GradleVersion.current().version
        val wrapperDir = dir("gradle/wrapper")
        wrapperDir.asFile.mkdirs()

        wrapperDir.file("gradle-wrapper.properties").asFile.writeText("distributionUrl=https\\://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip")
    }


    @TaskAction
    fun generate() {
        val root = destination.get()
        val buildLogicDir = root.dir("build-logic")

        buildLogicDir.addGradleWrapper()
        buildLogicDir.addBuildLogicSettingsFile(model.get().elementTypes.map { it.name })
        model.get().elementTypes.forEach { elementType ->
            val elementTypeDir = buildLogicDir.dir(elementType.name)
            elementTypeDir.updateBuildFile(listOf("kotlin-dsl"), "${model.get().swPackage.get()}.buildlogic",
                elementType.basedOn.get().filter { it.contains(":") }.map { it.split(":").let { id -> "\"${id[0]}:${id[0]}.gradle.plugin:${id[1]}\"" } })
            elementTypeDir.dir("src/main/kotlin").updateBuildFile(elementType.basedOn.get().map { if (it.contains(":")) it.substring(0, it.indexOf(":")) else it},
                model.get().swPackage.get(), emptyList(), "${model.get().swPackage.get()}.${elementType.name}")
        }

        model.get().components.forEach { component ->
            val componentDir = root.dir(component.name)
            componentDir.addGradleWrapper()

            componentDir.addComponentSettingsFile(component.dependsOn.get().map { it.name }, component.type.get().elements.map { it.name })

            component.type.get().elements.forEach { element ->
                val componentElementDir = componentDir.dir(element.name)
                componentElementDir.updateBuildFile(listOf("${model.get().swPackage.get()}.${element.type.get().name}"), "${'$'}group.${component.name}",
                    element.dependsOn.get().map { "project(\":${it.name}\")" }
                            + element.dependsOnAllExternal.get().map { (cType, e) -> model.get().components.filter { it.type.get() == cType }.map { c -> "\"${model.get().swPackage.get()}.${c.name}:${e.name}\"" }}.flatten()
                            + element.dependsOnExternal.get().map { (c, e) -> "\"${model.get().swPackage.get()}.${c.name}:${e.name}\"" }
                )
                componentElementDir.dir("src/main/${element.type.get().sourcesDir.get()}/${model.get().swPackage.get().replace('/', '.')}/${component.name}/${element.name}").asFile.mkdirs()
            }
        }
    }
}