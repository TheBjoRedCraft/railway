plugins {
    kotlin("jvm")
    `java-library`
    id("xyz.jpenilla.gremlin-gradle") version "0.0.9"
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
    api("org.spongepowered:configurate-core:4.2.0")
    runtimeDownload("org.spongepowered:configurate-core:4.2.0")
    runtimeDownload("org.spongepowered:configurate-gson:4.2.0")
    runtimeDownload("org.spongepowered:configurate-yaml:4.2.0")
    runtimeDownload("org.spongepowered:configurate-jackson:4.2.0")
    runtimeDownload("org.spongepowered:configurate-extra-kotlin:4.2.0")
    runtimeDownload("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
}