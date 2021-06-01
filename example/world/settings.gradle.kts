pluginManagement {
    includeBuild("../build-logic")
}
dependencyResolutionManagement {      
    repositories {
        mavenCentral()
    }
}

includeBuild("../canvas")

include("gamelogic")
include("graphics")
include("model")
