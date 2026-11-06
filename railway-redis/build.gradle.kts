plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.4.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.railwayConfig)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
    api("org.redisson:redisson:4.8.0")
}