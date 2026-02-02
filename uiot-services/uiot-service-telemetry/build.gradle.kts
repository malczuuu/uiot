plugins {
    id("internal.spring-app-convention")
}

dependencies {
    implementation(platform(project(":uiot-libraries:uiot-library-bom")))

    implementation(project(":uiot-libraries:uiot-library-models"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.kafka)
    implementation(libs.spring.boot.starter.webmvc)

    implementation(libs.micrometer.registry.prometheus)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.amqp.test)
    testImplementation(libs.spring.boot.starter.kafka.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
