plugins {
    id("java-library")
}

dependencies {
    api(platform(project(":uiot-libraries:uiot-library-bom")))

    api(libs.problem4j.core)
}
