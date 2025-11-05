import com.diffplug.spotless.LineEnding

plugins {
    id("org.springframework.boot").version("3.5.5").apply(false)
    id("io.spring.dependency-management").version("1.1.7").apply(false)
    id("com.diffplug.spotless").version("8.0.0")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    group = "io.github.malczuuu.uiot"

    /**
     * In order to avoid hardcoding snapshot versions, we derive the version from the current Git
     * commit hash. For CI/CD add -Pversion={releaseVersion} parameter to match Git tag.
     */
    version =
        if (version == "unspecified") {
            getSnapshotVersion(rootProject.rootDir)
        } else {
            version
        }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion = JavaLanguageVersion.of(17)
    }

    /**
     * Usage:
     *   ./gradlew printVersion
     */
    tasks.register("printVersion") {
        description = "Prints the current project version to the console"
        group = "help"
        doLast {
            println("${project.name} version: ${project.version}")
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Jar>().configureEach {
        manifest {
            attributes(mapOf("Implementation-Version" to project.version))
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    /**
     * Scripts that build Docker image require that there's only one jar.
     */
    pluginManager.withPlugin("org.springframework.boot") {
        tasks.withType<Jar>().configureEach {
            if (name != "bootJar") {
                enabled = false
            }
        }
    }
}

spotless {
    format("misc") {
        target("**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("yaml") {
        target("**/*.yml", "**/*.yaml")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
    }

    java {
        target("uiot-*/src/**/*.java")

        googleJavaFormat("1.28.0")
    }

    kotlinGradle {
        target("**/*.gradle.kts")

        ktlint()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}
