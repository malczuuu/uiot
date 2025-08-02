# uIoT Service Accounting

A microservice for resource usage accounting and billing analytics. This service processes streaming telemetry data to
track resource consumption, generate usage metrics, and provide billing information for IoT devices and rooms.

| Port   | Description |
|--------|-------------|
| `8331` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

## Configuration

| Property                                 | Description                                                               |
|------------------------------------------|---------------------------------------------------------------------------|
| `spring.data.mongodb.uri`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.             |
| `spring.data.mongodb.database`           | MongoDB database name, default: `uiot-service-accounting`.                |
| `spring.kafka.streams.bootstrap-servers` | Kafka bootstrap servers for stream processing, default: `localhost:9092`. |
| `uiot.metrics-topic`                     | Kafka topic for raw accounting metrics, default: `uiot-accounting`.       |
| `uiot.windows-topic`                     | Kafka topic for windowed metrics, default: `uiot-accounting-windows`.     |
| `uiot.windows-size`                      | Time window size for aggregation, default: `1m`.                          |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## REST API

Up-to-date API documentation is available at `/swagger-ui/index.html`. The OpenAPI specification is auto-generated.

## Stream Processing

The service gathers data from the `uiot-accounting` topic, which contains raw accounting metrics. These metrics may come
from various places in the entire system. The service aggregates these messages in a **time-windowed** fashion and
publishes them to the `uiot-accounting-windows` topic.

**Example accounting metric message:**

```json
{
  "type": "accounting_metric",
  "accounting_metric": {
    "uuid": "f9612419-d3f4-4a4b-a2cd-5aa61ccb3159",
    "type": "inbound_mqtt",
    "room_uid": "room1",
    "value": 12,
    "time": 1628939340,
    "tags": {
      "thing_uid": "thing1"
    }
  }
}
```
