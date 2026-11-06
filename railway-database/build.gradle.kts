plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.railwayConfig)

    api("org.jetbrains.exposed:exposed-core:1.6.0")
    api("org.jetbrains.exposed:exposed-r2dbc:1.6.0")

    implementation("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")
    implementation("org.postgresql:r2dbc-postgresql:1.1.3.RELEASE")
    implementation("org.mariadb:r2dbc-mariadb:1.4.2")
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