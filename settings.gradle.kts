pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "uiot"

include(":uiot-library-models")
include(":uiot-library-problems")
include(":uiot-service-accounting")
include(":uiot-service-connectivity")
include(":uiot-service-history")
include(":uiot-service-rooms")
include(":uiot-service-rules")
include(":uiot-service-telemetry")
include(":uiot-service-things")
