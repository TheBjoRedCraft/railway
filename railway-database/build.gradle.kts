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

val exposedVersion = "1.6.0"

dependencies {
    runtimeDownload("org.jetbrains.exposed:exposed-core:$exposedVersion")
    runtimeDownload("org.jetbrains.expowsed:exposed-r2dbc:$exposedVersion")

    runtimeDownload("org.postgresql:postgresql:42.7.8")
    runtimeDownload("org.mariadb.jdbc:mariadb-java-client:3.5.6")
}