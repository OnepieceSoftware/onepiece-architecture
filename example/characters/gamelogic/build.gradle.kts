plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.characters"

dependencies {
    implementation(project(":model"))
}
