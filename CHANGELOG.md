# Changelog

All notable changes to this project are documented in this file.

This changelog follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and uses [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

This release introduces a minimalistic, microservice-based IoT system. The system is composed of loosely coupled
services responsible for device management, telemetry processing, basic rules, and historical data access. Services are
implemented in Spring Boot and communicate via **Kafka** or internal REST API. Public REST APIs are exposed via a
centralized **Traefik** gateway.

Telemetry data from IoT devices is ingested via **RabbitMQ**, processed, and routed to storage or automation services.
The platform uses **MongoDB** as its primary datastore and is fully containerized using **Docker Compose** for local
and production environments. Code is organized as a Gradle **multi-module project** with shared libraries for data
models and error handling.

### Added

- Enable creation of rooms for separation of devices in logical groups (`uiot-service-rooms`).
- Enable registering and managing devices (`uiot-service-things`).
- Enable managing device connectivity and credentials (`uiot-service-connectivity`).
- Add HTTP API for backing `rabbitmq_auth_backend_http` **RabbitMQ** plugin (`uiot-service-connectivity`).
- Use **RabbitMQ** to collect telemetry data from edge devices and ingest it into the system (`uiot-service-telemetry`).
- Store and query historical telemetry and event data (`uiot-service-history`).
- Store in time windows data involving MQTT inbound traffic (`uiot-service-accounting`).
- Introduce basic (naive) rules engine for automating device behavior based on events (`uiot-service-rules`).
- Introduce inter-service communication via **Kafka** for distributed resources management (e.g. creation or cleanup).
- Containerize all services and provide **Docker Compose** setups for development and production.
- Route all HTTP traffic through API gateway with **Traefik**.
- Export APIs with **OpenAPI** specification and **Swagger UI**, accessible from main **Traefik** gateway.
- Format and validate code using **Google Java Format**.
- Build and manage all services in a single **Gradle** multi-module structure.
- Document service usage, local deployment setup, and messaging infrastructure.
