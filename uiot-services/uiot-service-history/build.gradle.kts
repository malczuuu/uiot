plugins {
    id("internal.spring-app-convention")
}

dependencies {
    implementation(platform(project(":uiot-libraries:uiot-library-bom")))

    implementation(project(":uiot-libraries:uiot-library-model"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.mongodb)
    implementation(libs.spring.boot.starter.kafka)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webmvc)

    implementation(libs.commons.codec)
    implementation(libs.kafka.streams)
    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.problem4j.spring.webmvc)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(project(":uiot-libraries:uiot-library-testkit"))
    testRuntimeOnly(libs.junit.platform.launcher)
}
