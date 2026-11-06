plugins {
    kotlin("jvm")
    id("xyz.jpenilla.gremlin-gradle") version "0.0.9"
    `java-library`
    `maven-publish`
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

    compileOnlyApi("org.jetbrains.exposed:exposed-core:1.6.0")
    compileOnlyApi("org.jetbrains.exposed:exposed-r2dbc:1.6.0")

    runtimeDownload("org.jetbrains.exposed:exposed-core:1.6.0")
    runtimeDownload("org.jetbrains.exposed:exposed-r2dbc:1.6.0")

    runtimeDownload("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")
    runtimeDownload("org.postgresql:r2dbc-postgresql:1.1.3.RELEASE")
    runtimeDownload("org.mariadb:r2dbc-mariadb:1.4.2")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}