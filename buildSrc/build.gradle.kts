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
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.4.0.202509020913-r")
}
