plugins {
    `kotlin-dsl`
}

java {
    // Kotlin does not yet support 25 JDK target
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.5.0.202512021534-r")
}
