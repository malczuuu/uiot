plugins {
    id("java-library")
}

dependencies {
    implementation(platform(libs.problem4j.spring.bom))

    compileOnly(libs.problem4j.core)
}
