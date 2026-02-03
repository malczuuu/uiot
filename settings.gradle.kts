pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "uiot"

include(":uiot-libraries:uiot-library-bom")
include(":uiot-libraries:uiot-library-model")
include(":uiot-libraries:uiot-library-testkit")

include(":uiot-services:uiot-service-accounting")
include(":uiot-services:uiot-service-connectivity")
include(":uiot-services:uiot-service-history")
include(":uiot-services:uiot-service-room")
include(":uiot-services:uiot-service-rule")
include(":uiot-services:uiot-service-telemetry")
include(":uiot-services:uiot-service-thing")
