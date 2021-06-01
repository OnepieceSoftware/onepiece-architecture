plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.characters"

dependencies {
    implementation(project(":model"))
    implementation("com.example.canvas:interfaces")
}
