plugins {
    id("java-library")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    compileOnly(platform(project(":uiot-libraries:uiot-library-bom")))

    compileOnly(libs.jackson.databind)
}
