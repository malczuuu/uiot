plugins {
    id("java-library")
}

dependencies {
    compileOnly(platform(project(":uiot-libraries:uiot-library-bom")))

    compileOnly(libs.jackson.databind)
}
