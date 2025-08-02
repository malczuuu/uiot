# uIoT Service History

A microservice that processes telemetry data streams and provides historical data storage and retrieval. This service
consumes real-time telemetry events, stores them for historical analysis, and maintains last-known state information for
all IoT devices.

| Port   | Description |
|--------|-------------|
| `8333` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

## Configuration

| Property                                 | Description                                                                             |
|------------------------------------------|-----------------------------------------------------------------------------------------|
| `spring.data.mongodb.uri`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.                           |
| `spring.data.mongodb.database`           | MongoDB database name, default: `uiot-service-history`.                                 |
| `spring.kafka.streams.bootstrap-servers` | Kafka bootstrap servers for stream processing, default: `localhost:9092`.               |
| `uiot.thing-events-topic`                | Kafka topic for incoming telemetry events, default: `uiot-thing-events`.                |
| `uiot.thing-metadata-topic`              | Kafka topic for thing metadata, default: `uiot-service-history_thing-metadata`.         |
| `uiot.keyed-thing-events-topic`          | Kafka topic for keyed thing events, default: `uiot-service-history_keyed-thing-events`. |
| `uiot.system-events-topic`               | Kafka topic for system events. See [Room Events](#room-events).                         |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## REST API

Up-to-date API documentation is available at `/swagger-ui/index.html`. The OpenAPI specification is auto-generated.

## Stream Processing

The service gathers data from the `uiot-thing-events` topic, which contains telemetry events. It processes these events
to create historical records and maintain the last-known state of devices. Historical records are stored in MongoDB,
while the last-known state is maintained in a KTable for quick access.

## Room Events

This service listens to room creation and deletion events on the Kafka topic to initialize or clean up historical data
storage. The input topic is named `uiot-system-events` by default.

**Example room creation event:**

```json
{
  "type": "room_create",
  "room_create": {
    "room_uid": "{room}"
  }
}
```

**Example room deletion event:**

```json
{
  "type": "room_delete",
  "room_delete": {
    "room_uid": "{room}"
  }
}
```
