plugins {
    id("java-library")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    compileOnly(libs.jackson.databind)
}
