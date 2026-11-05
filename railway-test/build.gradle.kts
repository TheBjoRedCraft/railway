plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.railwayRedis)
    implementation(projects.railwayDatabase)
}