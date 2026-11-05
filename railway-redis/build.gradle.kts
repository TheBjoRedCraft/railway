plugins {
    kotlin("jvm")
    id("xyz.jpenilla.gremlin-gradle") version "0.0.9"
    kotlin("plugin.serialization") version "2.4.10"
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.runtimeDownload.get())
    }
}

dependencies {
    implementation(projects.railwayConfig)
    runtimeDownload("org.redisson:redisson:4.8.0")
    runtimeDownload("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
    runtimeDownload("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
}