plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    implementation(platform(project(":uiot-libraries:uiot-library-bom")))

    implementation(project(":uiot-libraries:uiot-library-models"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.web)

    implementation(libs.micrometer.registry.prometheus)

    implementation(libs.spring.kafka)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.rabbit.test)
    testImplementation(libs.spring.kafka.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
