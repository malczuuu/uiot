# uIoT Service Rooms

A microservice for room management that serves as the top-level hierarchy container in the uIoT system. Rooms contain
Things (IoT devices) and provide organizational structure for the entire system.

| Port   | Description |
|--------|-------------|
| `8334` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

## Configuration

| Property                                 | Description                                                               |
|------------------------------------------|---------------------------------------------------------------------------|
| `spring.mongodb.uri`                     | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.             |
| `spring.mongodb.database`                | MongoDB database name, default: `uiot-service-room`.                      |
| `spring.kafka.streams.bootstrap-servers` | Kafka bootstrap servers for stream processing, default: `localhost:9092`. |
| `uiot.system-events-topic`               | Kafka topic for system events. See [Room Events](#room-events).           |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## REST API

Up-to-date API documentation is available at `/swagger-ui/index.html`. The OpenAPI specification is auto-generated.

## Room Events

This service uses an event-first approach: all room operations are published as events on the Kafka topic to allow other
services to react to changes in room state. The service itself consumes these events and creates or deletes rooms
asynchronously. The topic is named `uiot-system-events` by default.

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
