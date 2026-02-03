import org.jetbrains.gradle.ext.Application
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings

plugins {
    id("org.jetbrains.gradle.plugin.idea-ext")
}

idea {
    project {
        settings {
            runConfigurations {
                create<Application>("launch uiot-service-accounting") {
                    mainClass = "io.github.malczuuu.uiot.accounting.AccountingApplication"
                    moduleName = "uiot.uiot-services.uiot-service-accounting.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch uiot-service-connectivity") {
                    mainClass = "io.github.malczuuu.uiot.connectivity.ConnectivityApplication"
                    moduleName = "uiot.uiot-services.uiot-service-connectivity.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch uiot-service-history") {
                    mainClass = "io.github.malczuuu.uiot.history.HistoryApplication"
                    moduleName = "uiot.uiot-services.uiot-service-history.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch uiot-service-room") {
                    mainClass = "io.github.malczuuu.uiot.room.RoomApplication"
                    moduleName = "uiot.uiot-services.uiot-service-room.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch uiot-service-rule") {
                    mainClass = "io.github.malczuuu.uiot.rule.RuleApplication"
                    moduleName = "uiot.uiot-services.uiot-service-rule.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch uiot-service-telemetry") {
                    mainClass = "io.github.malczuuu.uiot.rabbitmq.telemetry.RabbitTelemetryApplication"
                    moduleName = "uiot.uiot-services.uiot-service-telemetry.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch uiot-service-thing") {
                    mainClass = "io.github.malczuuu.uiot.thing.ThingApplication"
                    moduleName = "uiot.uiot-services.uiot-service-thing.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Gradle>("build project") {
                    taskNames = listOf("spotlessApply build")
                    projectPath = rootProject.rootDir.absolutePath
                }
                create<Gradle>("test project") {
                    taskNames = listOf("check --rerun-tasks")
                    projectPath = rootProject.rootDir.absolutePath
                }
                create<Gradle>("test project [with containers]") {
                    taskNames = listOf("check --rerun-tasks -Pcontainers.enabled")
                    projectPath = rootProject.rootDir.absolutePath
                }
            }
        }
    }
}
