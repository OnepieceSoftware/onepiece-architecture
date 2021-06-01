plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.game"

dependencies {
    implementation(project(":state"))
    implementation(project(":gameloop"))
    implementation(project(":graphicsengine"))
}
