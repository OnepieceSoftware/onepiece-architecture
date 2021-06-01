package software.onepiece.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

interface SoftwareArchModel {
    val swPackage: Property<String>

    @get:Nested
    val elementTypes: NamedDomainObjectContainer<ElementType>
    @get:Nested
    val componentTypes: NamedDomainObjectContainer<ComponentType>
    @get:Nested
    val components: NamedDomainObjectContainer<Component>
}