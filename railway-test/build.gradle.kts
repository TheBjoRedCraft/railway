plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.railwayRedis)
    implementation(projects.railwayDatabase)
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.jetbrains.exposed:exposed-core:1.6.0")
    implementation("org.jetbrains.exposed:exposed-r2dbc:1.6.0")

    implementation("org.jetbrains.exposed:exposed-core:1.6.0")
    implementation("org.jetbrains.exposed:exposed-r2dbc:1.6.0")

    implementation("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")
    implementation("org.postgresql:r2dbc-postgresql:1.1.3.RELEASE")
    implementation("org.mariadb:r2dbc-mariadb:1.4.2")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")
}