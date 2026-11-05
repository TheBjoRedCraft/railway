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
    implementation(projects.railwayConfig)

    runtimeDownload("org.jetbrains.exposed:exposed-core:1.6.0")
    runtimeDownload("org.jetbrains.exposed:exposed-r2dbc:1.6.0")

    runtimeDownload("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")
    runtimeDownload("org.postgresql:r2dbc-postgresql:1.1.3.RELEASE")
    runtimeDownload("org.mariadb:r2dbc-mariadb:1.4.2")
}