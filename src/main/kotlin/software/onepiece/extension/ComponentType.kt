package software.onepiece.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.Nested

interface ComponentType {
    val name: String
    @get:Nested
    val elements: NamedDomainObjectContainer<Element>
}