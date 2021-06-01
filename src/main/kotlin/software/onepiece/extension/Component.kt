package software.onepiece.extension

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface Component {
    val name: String
    val type: Property<ComponentType>
    val dependsOn: ListProperty<Component> // TODO remove and infer this instead
}