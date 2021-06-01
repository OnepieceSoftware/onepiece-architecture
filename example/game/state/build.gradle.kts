plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.game"

dependencies {
    implementation("com.example.characters:model")
    implementation("com.example.world:model")
}
