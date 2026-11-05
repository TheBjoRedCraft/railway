rootProject.name = "railway"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("railway-database")
include("railway-config")
include("railway-redis")
include("railway-redis:railway-redis-ksp")