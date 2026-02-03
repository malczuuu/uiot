# uIoT Library Models

A shared library containing common DTOs used by RabbitMQ and Kafka producers and ocnsumers across the entire uIoT
system. This library ensures consistency in data structures and reduces code duplication between services.

## Usage

This library is included as a dependency in all uIoT microservices.

```kotlin
dependencies {
    implementation(project(":uiot-libraries:uiot-library-model"))
}
```
