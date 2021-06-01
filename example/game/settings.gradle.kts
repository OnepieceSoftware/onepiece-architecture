pluginManagement {
    includeBuild("../build-logic")
}
dependencyResolutionManagement {      
    repositories {
        mavenCentral()
    }
}

includeBuild("../canvas")
includeBuild("../characters")
includeBuild("../world")

include("gameloop")
include("graphicsengine")
include("main")
include("state")
