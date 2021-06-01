plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.game"

dependencies {
    implementation(project(":state"))
    implementation("com.example.characters:graphics")
    implementation("com.example.world:graphics")
    implementation("com.example.canvas:interfaces")
}
