# uIoT Service Telemetry

A microservice for telemetry data ingestion from MQTT devices. This service acts as a bridge between RabbitMQ (MQTT) and
Apache Kafka, processing incoming telemetry messages and forwarding them to the event streaming platform.

| Port   | Description |
|--------|-------------|
| `8336` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

> **Note:** This service does not expose HTTP API endpoints; it operates as a background data processor.

## Configuration

| Property                                  | Description                                                                    |
|-------------------------------------------|--------------------------------------------------------------------------------|
| `spring.rabbitmq.addresses`               | RabbitMQ connection addresses, default: `localhost:5672`.                      |
| `spring.rabbitmq.username`                | RabbitMQ username, default: `user`.                                            |
| `spring.rabbitmq.password`                | RabbitMQ password, default: `user`.                                            |
| `spring.kafka.producer.bootstrap-servers` | Kafka bootstrap servers for producing events, default: `localhost:9092`.       |
| `uiot.rabbitmq-routing-key-regexp`        | Regex pattern for parsing routing keys, default: `^telemetry\.(.*)\.(.*)$`.    |
| `uiot.rabbitmq-routing-key`               | RabbitMQ routing key pattern to listen to, default: `telemetry.*.*`.           |
| `uiot.rabbitmq-input-queue`               | RabbitMQ queue name for incoming telemetry, default: `uiot-telemetry-inbound`. |
| `uiot.kafka-thing-events-topic`           | Kafka topic for thing events, default: `uiot-thing-events`.                    |
| `uiot.kafka-accounting-topic`             | Kafka topic for accounting events, default: `uiot-accounting`.                 |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## Message Flow

### Input (RabbitMQ/MQTT)

- **Queue:** `uiot-telemetry-inbound`
- **Routing Key Pattern:** `telemetry.{room_uid}.{thing_uid}`
- **Exchange:** `amq.topic`

This corresponds to MQTT messages published by devices to `telemetry/{room_uid}/{thing_uid}` topic.

### Output (Kafka)

1. **Thing Events Topic** (`uiot-thing-events`): Structured telemetry events for historical storage.
2. **Accounting Topic** (`uiot-accounting`): Resource usage metrics for billing/monitoring.
