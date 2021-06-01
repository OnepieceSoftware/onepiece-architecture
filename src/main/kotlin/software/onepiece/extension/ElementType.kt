package software.onepiece.extension

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface ElementType {
    val name: String
    val basedOn: ListProperty<String>
    val sourcesDir: Property<String>
}