plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.game"

dependencies {
    implementation(project(":state"))
    implementation("com.example.characters:gamelogic")
    implementation("com.example.world:gamelogic")
}
