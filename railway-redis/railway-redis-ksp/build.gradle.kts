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
    runtimeDownload("com.google.devtools.ksp:symbol-processing-api:2.0.21-1.0.25")
}