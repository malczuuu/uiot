plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":uiot-libraries:uiot-library-bom")))

    api(libs.jackson.databind)
    api(libs.problem4j.core)
}
