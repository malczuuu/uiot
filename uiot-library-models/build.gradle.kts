plugins {
    id("java-library")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    compileOnly(libs.jackson.databind)
}
