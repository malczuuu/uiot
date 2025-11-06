plugins {
    id("java-library")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    implementation(platform(libs.problem4j.spring.bom))

    compileOnly(libs.problem4j.core)
}
