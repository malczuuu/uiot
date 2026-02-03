plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":uiot-libraries:uiot-library-bom")))

    api(libs.spring.boot.resttestclient)
    api(libs.spring.boot.starter.amqp.test)
    api(libs.spring.boot.starter.kafka.test)
    api(libs.spring.boot.starter.test)
    api(libs.spring.boot.testcontainers)
    api(libs.testcontainers.kafka)
    api(libs.testcontainers.mongodb)
    api(libs.testcontainers.rabbitmq)
}
