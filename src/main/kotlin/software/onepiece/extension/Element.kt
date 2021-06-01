package software.onepiece.extension

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface Element {
    val name: String
    val type: Property<ElementType>
    val dependsOn: ListProperty<Element>
    val dependsOnExternal: ListProperty<Pair<Component, Element>> // TODO make this part of the element type
    val dependsOnAllExternal: ListProperty<Pair<ComponentType, Element>> // TODO make this part of the element type
}