import com.diffplug.spotless.LineEnding

buildscript {
    dependencies {
        // version included via libs.plugins.spring.boot is affected by CVE-2025-48924
        classpath(libs.commons.lang3)
    }
}

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot).apply(false)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
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
    pluginManager.withPlugin(rootProject.libs.plugins.spring.boot.get().pluginId) {
        tasks.withType<Jar>().matching { it.name != "bootJar" }.configureEach {
            enabled = false
        }
    }
}

spotless {
    java {
        target("**/src/**/*.java")

        googleJavaFormat("1.28.0")
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlin {
        target("**/src/**/*.kt")

        ktfmt("0.59").metaStyle()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlinGradle {
        target("**/*.gradle.kts")

        ktlint("1.7.1").editorConfigOverride(mapOf("max_line_length" to "120"))
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("yaml") {
        target("**/*.yml", "**/*.yaml")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("misc") {
        target("**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}
