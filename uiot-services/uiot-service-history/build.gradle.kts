plugins {
    id("java")
    alias(libs.plugins.spring.boot)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    implementation(platform(project(":uiot-libraries:uiot-library-bom")))

    implementation(project(":uiot-libraries:uiot-library-models"))
    implementation(project(":uiot-libraries:uiot-library-problems"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.mongodb)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    implementation(libs.micrometer.registry.prometheus)

    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    implementation(libs.problem4j.spring.webmvc)

    implementation(libs.spring.kafka)
    implementation(libs.kafka.streams)

    implementation(libs.commons.codec)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
