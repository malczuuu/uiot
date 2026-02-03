plugins {
    id("internal.spring-app-convention")
}

dependencies {
    implementation(platform(project(":uiot-libraries:uiot-library-bom")))

    implementation(project(":uiot-libraries:uiot-library-model"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.kafka)
    implementation(libs.spring.boot.starter.webmvc)

    implementation(libs.micrometer.registry.prometheus)

    testImplementation(project(":uiot-libraries:uiot-library-testkit"))
    testRuntimeOnly(libs.junit.platform.launcher)
}
