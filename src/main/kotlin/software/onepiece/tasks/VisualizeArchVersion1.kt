package software.onepiece.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import software.onepiece.extension.SoftwareArchModel

abstract class VisualizeArchVersion1 : DefaultTask() {

    @get:Internal
    abstract val model: Property<SoftwareArchModel>

    @get:Internal
    abstract val diagramFile: RegularFileProperty

    @TaskAction
    fun visualize() {
        val components = model.get().components.joinToString("\n") { "[${it.name}] <<${it.type.get().name}>> as ${it.name}" }
        val dependencies = model.get().components.map { from -> from.dependsOn.get().map { to -> "${from.name} -down-> ${to.name}" } }.flatten().joinToString("\n")

        val componentTypes = model.get().componentTypes.joinToString("\n") { cT ->
            "folder ${cT.name} {\n" +
                    cT.elements.joinToString("\n") { "  [${it.name}] <<${it.type.get().name}>> as ${cT.name}.${it.name}" } +
            "\n}"
        }
        val elementTypes = "folder \"build-logic\" {\n" +
            model.get().elementTypes.joinToString("\n") { "  [${it.name}] <<gradle-plugin>> as ${it.name.replace('-', '_')}" } +
        "\n}"

        val inComponentDependencies = model.get().componentTypes.map { cT -> cT.elements.map { from -> from.dependsOn.get().map {
                to -> "${cT.name}.${from.name} --> ${cT.name}.${to.name}" } }.flatten() }.flatten().joinToString("\n")
        val crossComponentDependencies = model.get().componentTypes.map { cT -> cT.elements.map { from -> from.dependsOnExternal.get().map {
                (toComp, to) -> "${cT.name}.${from.name} --> ${toComp.type.get().name}.${to.name} : ${toComp.name}" } }.flatten() }.flatten().joinToString("\n")
        val crossComponentAllDependencies = model.get().componentTypes.map { cT -> cT.elements.map { from -> from.dependsOnAllExternal.get().map {
                (toComp, to) -> "${cT.name}.${from.name} --> ${toComp.name}.${to.name}" } }.flatten() }.flatten().joinToString("\n")
        val basedOn = model.get().elementTypes.map { from -> from.basedOn.get().map {
                to -> "${from.name.replace('-', '_')} --> [${to.replace(":.*".toRegex(), "")}]" } }.flatten().joinToString("\n")
        val diagram = """
@startuml
skinparam monochrome true
skinparam componentStyle uml1
skinparam component {
    BackgroundColor white
    BorderColor Black
	ArrowColor Black
}

$components

$elementTypes

$dependencies

$componentTypes

$inComponentDependencies

$crossComponentDependencies

$crossComponentAllDependencies

$basedOn

@enduml
"""
        diagramFile.get().asFile.parentFile.mkdirs()
        diagramFile.get().asFile.writeText(diagram)
    }
}