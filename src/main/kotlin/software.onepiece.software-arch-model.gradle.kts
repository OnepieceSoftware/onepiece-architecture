import software.onepiece.extension.SoftwareArchModel
import software.onepiece.tasks.UpdateArch
import software.onepiece.tasks.VisualizeArch

val arch = extensions.create<SoftwareArchModel>("arch")

tasks.register<UpdateArch>("updateArch") {
    model.convention(arch)
    destination.convention(layout.projectDirectory)
}
tasks.register<VisualizeArch>("visualizeArch") {
    model.convention(arch)
    diagramFolder.convention(layout.buildDirectory.dir("diagrams"))
}