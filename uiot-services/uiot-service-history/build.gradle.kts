plugins {
    id("internal.spring-app-convention")
}

dependencies {
    implementation(platform(project(":uiot-libraries:uiot-library-bom")))

    implementation(project(":uiot-libraries:uiot-library-models"))
    implementation(project(":uiot-libraries:uiot-library-problems"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.mongodb)
    implementation(libs.spring.boot.starter.kafka)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webmvc)

    implementation(libs.micrometer.registry.prometheus)

    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    implementation(libs.problem4j.spring.webmvc)

    implementation(libs.kafka.streams)

    implementation(libs.commons.codec)

    testImplementation(project(":uiot-libraries:uiot-library-testkit"))
    testRuntimeOnly(libs.junit.platform.launcher)
}
