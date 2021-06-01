package software.onepiece.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import software.onepiece.extension.SoftwareArchModel

abstract class VisualizeArch : DefaultTask() {

    @get:Internal
    abstract val model: Property<SoftwareArchModel>

    @get:Internal
    abstract val diagramFolder: DirectoryProperty

    // skinparam backgroundcolor transparent
    private val header = """@startuml
skinparam monochrome true
skinparam componentStyle uml1
skinparam component {
    BackgroundColor white
    BorderColor Black
	ArrowColor Black
}"""


    @TaskAction
    fun visualize() {
        diagramFolder.get().asFile.mkdirs()
        visualizeComponents()
        visualizeComponentTypes()
        visualizeBuildLogic()
        visualizeDetails()
    }

    private fun visualizeComponents() {
        val components = model.get().components.joinToString("\n") { c ->
            "component ${c.name} <<${c.type.get().name}>> as ${c.name}"
        }
        val dependencies = model.get().components.map { from -> from.dependsOn.get().map { to -> "${from.name} -down-> ${to.name}" } }.flatten().joinToString("\n")

        val diagram = """
$header

$components

$dependencies

@enduml
"""
        diagramFolder.file("components.puml").get().asFile.writeText(diagram)
    }

    private fun visualizeComponentTypes() {
        val componentTypes = model.get().componentTypes.joinToString("\n") { cT ->
            "folder ${cT.name} {\n" +
                    cT.elements.joinToString("\n") { "  [${it.name}] <<${it.type.get().name}>> as ${cT.name}.${it.name}" } +
                    "\n}"
        }
        val inComponentDependencies = model.get().componentTypes.map { cT -> cT.elements.map { from -> from.dependsOn.get().map {
                to -> "${cT.name}.${from.name} -down-> ${cT.name}.${to.name}" } }.flatten() }.flatten().joinToString("\n")
        val crossComponentDependencies = model.get().componentTypes.map { cT -> cT.elements.map { from -> from.dependsOnExternal.get().map {
                (toComp, to) -> "${cT.name}.${from.name} ---> ${toComp.type.get().name}.${to.name} : ${toComp.name}" } }.flatten() }.flatten().joinToString("\n")
        val crossComponentAllDependencies = model.get().componentTypes.map { cT -> cT.elements.map { from -> from.dependsOnAllExternal.get().map {
                (toComp, to) -> "${cT.name}.${from.name} ---> ${toComp.name}.${to.name}" } }.flatten() }.flatten().joinToString("\n")

        val diagram = """
$header

$componentTypes

$inComponentDependencies

$crossComponentDependencies

$crossComponentAllDependencies

@enduml
"""
        diagramFolder.file("component-types.puml").get().asFile.writeText(diagram)
    }

    private fun visualizeBuildLogic() {
        val elementTypes = "folder \"build-logic\" {\n" +
                model.get().elementTypes.joinToString("\n") { "  [${it.name}] <<gradle-plugin>> as ${it.name.replace('-', '_')}" } +
                "\n}"
        val basedOn = model.get().elementTypes.map { from -> from.basedOn.get().map {
                to -> "${from.name.replace('-', '_')} --> [${to.replace(":.*".toRegex(), "")}]" } }.flatten().joinToString("\n")

        val diagram = """
$header

$elementTypes

$basedOn

@enduml
"""
        diagramFolder.file("build-logic.puml").get().asFile.writeText(diagram)
    }

    private fun visualizeDetails() {
        val components = model.get().components.joinToString("\n") { c ->
            "component ${c.name} <<${c.type.get().name}>> as ${c.name} {\n" +
                    c.type.get().elements.joinToString("\n") { "  [${it.name}] <<${it.type.get().name}>> as ${c.name}.${it.name}" } +
            "\n}"
        }

        val inComponentDependencies = model.get().components.map { c -> c.type.get().elements.map { from -> from.dependsOn.get().map {
                to -> "${c.name}.${from.name} -down-> ${c.name}.${to.name}" } }.flatten() }.flatten().joinToString("\n")
        val crossComponentDependencies = model.get().components.map { c -> c.type.get().elements.map { from -> from.dependsOnExternal.get().map {
                (toComp, to) -> "${c.name}.${from.name} -down--> ${toComp.name}.${to.name}" } }.flatten() }.flatten().joinToString("\n")
        val crossComponentAllDependencies = model.get().components.map { c -> c.type.get().elements.map { from -> from.dependsOnAllExternal.get().map {
                (toCompType, to) -> model.get().components.filter { it.type.get() == toCompType }.map { toComp -> "${c.name}.${from.name} -down--> ${toComp.name}.${to.name}" } }.flatten() }.flatten() }.flatten().joinToString("\n")

        val diagram = """
$header

$components

$inComponentDependencies

$crossComponentDependencies

$crossComponentAllDependencies

@enduml
"""
        diagramFolder.file("details.puml").get().asFile.writeText(diagram)
    }
}