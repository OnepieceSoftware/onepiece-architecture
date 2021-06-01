plugins {
    id("com.example.my-kotlin-library")
}

group = "$group.world"

dependencies {
    implementation(project(":model"))
}
