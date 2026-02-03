# uIoT Service Rules

A microservice implementing a basic rule engine for automated actions based on telemetry data. This service processes
incoming telemetry events and publishes actions on the `uiot-action-execution-events` topic if criteria are matched.

| Port   | Description |
|--------|-------------|
| `8335` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

## Configuration

| Property                                 | Description                                                                       |
|------------------------------------------|-----------------------------------------------------------------------------------|
| `spring.data.mongodb.uri`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.                     |
| `spring.data.mongodb.database`           | MongoDB database name, default: `uiot-service-rule`.                              |
| `spring.kafka.streams.bootstrap-servers` | Kafka bootstrap servers for stream processing, default: `localhost:9092`.         |
| `uiot.system-events-topic`               | Kafka topic for system events. See [Room Events](#room-events).                   |
| `uiot.thing-events-topic`                | Kafka topic for incoming telemetry events, default: `uiot-thing-events`.          |
| `uiot.action-execution-events.topic`     | Kafka topic for action execution events, default: `uiot-action-execution-events`. |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## REST API

Up-to-date API documentation is available at `/swagger-ui/index.html`. The OpenAPI specification is auto-generated.

## Stream Processing

The service listens to the `uiot-thing-events` topic, which contains telemetry events. For each event, it checks if any
rules match the event's criteria. If a match is found, it publishes an action execution event to the
`uiot-action-execution-events` topic. If no rules match, the event is ignored.

> **Note:** This feature queries the database for every new event, so it is experimental and may not be suitable for
> high-throughput scenarios.

## Room Events

This service listens to room deletion events on the Kafka topic for cleanup operations. The input topic is named
`uiot-system-events` by default.

**Example room deletion event:**

```json
{
  "type": "room_delete",
  "room_delete": {
    "room_uid": "{room}"
  }
}
```

When a room is deleted, all associated rules are removed.
