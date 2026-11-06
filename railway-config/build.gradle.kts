plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
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

dependencies {
    api("org.spongepowered:configurate-core:4.2.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation("org.spongepowered:configurate-gson:4.2.0")
    implementation("org.spongepowered:configurate-yaml:4.2.0")
    implementation("org.spongepowered:configurate-jackson:4.2.0")
    implementation("org.spongepowered:configurate-extra-kotlin:4.2.0")
}