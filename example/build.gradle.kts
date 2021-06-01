plugins {
    id("software.onepiece.software-arch-model")
}

arch {
    swPackage.set("com.example")

    val springBootApplication = elementTypes.create("my-spring-boot-application") {
        basedOn.add("java-library")
        sourcesDir.set("java")
    }
    val kotlinLibrary = elementTypes.create("my-kotlin-library") {
        basedOn.add("org.jetbrains.kotlin.jvm:1.4.32")
        basedOn.add("java-library")
        sourcesDir.set("kotlin")
    }

    val platformComponent = componentTypes.create("platform") {
        elements {
            create("interfaces") {
                type.set(kotlinLibrary)
            }
        }
    }

    val canvas by components.creating {
        type.set(platformComponent)
    }

    val gameSystemComponent = componentTypes.create("system") {
        elements {
            val model = create("model") {
                type.set(kotlinLibrary)
            }
            create("gamelogic") {
                type.set(kotlinLibrary)
                dependsOn.add(model)
            }
            create("graphics") {
                type.set(kotlinLibrary)
                dependsOn.add(model)
                dependsOnExternal.add(Pair(canvas, platformComponent.elements["interfaces"]))
            }
        }
    }

    val adapterComponent = componentTypes.create("adapter") {
        elements {
            val state = create("state") {
                type.set(kotlinLibrary)
                dependsOnAllExternal.add(Pair(gameSystemComponent, gameSystemComponent.elements["model"]))
            }
            val gameloop = create("gameloop") {
                type.set(kotlinLibrary)
                dependsOn.add(state)
                // dependsOnAllExternal.add(Pair(gameSystemComponent, gameSystemComponent.elements["model"]))
                dependsOnAllExternal.add(Pair(gameSystemComponent, gameSystemComponent.elements["gamelogic"]))
            }
            val graphicsengine = create("graphicsengine") {
                type.set(kotlinLibrary)
                dependsOn.add(state)
                dependsOnExternal.add(Pair(canvas, platformComponent.elements["interfaces"]))
                // dependsOnAllExternal.add(Pair(gameSystemComponent, gameSystemComponent.elements["model"]))
                dependsOnAllExternal.add(Pair(gameSystemComponent, gameSystemComponent.elements["graphics"]))
            }
            create("main") {
                type.set(kotlinLibrary)
                dependsOn.add(state)
                dependsOn.add(gameloop)
                dependsOn.add(graphicsengine)
            }
        }
    }

    components {
        val world by creating {
            type.set(gameSystemComponent)
            dependsOn.add(canvas)
        }
        val characters by creating {
            type.set(gameSystemComponent)
            dependsOn.add(canvas)
        }
        // val monsters by creating {
        //     type.set(gameSystemComponent)
        // }
        val game by creating {
            type.set(adapterComponent)
            dependsOn.add(canvas)
            dependsOn.add(characters)
            dependsOn.add(world)
        }
    }
}
