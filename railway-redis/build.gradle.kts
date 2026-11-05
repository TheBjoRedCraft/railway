plugins {
    kotlin("jvm")
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
    implementation(projects.railwayConfig)
    runtimeDownload("org.redisson:redisson:4.8.0")
}