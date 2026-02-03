# uIoT Service Things

A microservice for IoT device (Thing) management within the uIoT system. This service handles the lifecycle of IoT
devices, which are organized within rooms and represent physical devices that send telemetry data.

| Port   | Description |
|--------|-------------|
| `8337` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

## Configuration

| Property                                 | Description                                                                                             |
|------------------------------------------|---------------------------------------------------------------------------------------------------------|
| `spring.data.mongodb.uri`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.                                           |
| `spring.data.mongodb.database`           | MongoDB database name, default: `uiot-service-thing`.                                                   |
| `spring.kafka.streams.bootstrap-servers` | Kafka bootstrap servers for stream processing, default: `localhost:9092`.                               |
| `uiot.connectivity.integration-url`      | Integration URL for connectivity management. See [Connectivity Integration](#connectivity-integration). |
| `uiot.system-events-topic`               | Kafka topic for system events. See [Room Events](#room-events).                                         |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## REST API

Up-to-date API documentation is available at `/swagger-ui/index.html`. The OpenAPI specification is auto-generated.

## Connectivity Integration

When a thing is deleted, this service requests connectivity cleanup from the `uiot-service-connectivity` via direct REST
API call. This ensures that device authentication credentials and MQTT permissions are properly removed.

**Integration URL:** Configurable via `UIOT_CONNECTIVITY_INTEGRATION_URL`

## Room Events

This service listens to room deletion events on the Kafka topic to clean up all things associated with a deleted room.
The input topic is named `uiot-system-events` by default.

**Example room deletion event:**

```json
{
  "type": "room_delete",
  "room_delete": {
    "room_uid": "{room}"
  }
}
```
