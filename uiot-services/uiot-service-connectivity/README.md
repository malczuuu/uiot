# uIoT Service Connectivity

A microservice for IoT device connectivity management and RabbitMQ integration. This service manages device
authentication credentials and integrates with RabbitMQ for MQTT authorization.

| Port   | Description |
|--------|-------------|
| `8332` | HTTP API    |

> **Note:** In Docker, the HTTP API is served on port `8080`.

## Configuration

| Property                                 | Description                                                               |
|------------------------------------------|---------------------------------------------------------------------------|
| `spring.mongodb.uri`                     | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.             |
| `spring.mongodb.database`                | MongoDB database name, default: `uiot-service-connectivity`.              |
| `spring.kafka.streams.bootstrap-servers` | Kafka bootstrap servers for stream processing, default: `localhost:9092`. |
| `uiot.vhost`                             | RabbitMQ global vhost for things authorization, default: `/`.             |
| `uiot.routing-key-template`              | RabbitMQ telemetry routing key template, default: `telemetry.%s.%s`.      |
| `uiot.username-context-separator`        | RabbitMQ username context separator, default: `.`.                        |
| `uiot.system-events-topic`               | Kafka topic for system events. See [Room Events](#room-events).           |

Properties can also be overridden via Environment Variables or Config Trees. See Spring Boot documentation for details.

## REST API

Up-to-date API documentation is available at `/swagger-ui/index.html`. The OpenAPI specification is auto-generated.

## RabbitMQ Integration

This service provides HTTP endpoints for the [
`rabbitmq_auth_backend_http`](https://www.rabbitmq.com/docs/auth-backend-http) plugin. Based on configured connectivity,
it allows IoT devices to publish messages to RabbitMQ using the MQTT protocol.

To publish telemetry data, devices must connect to RabbitMQ with the following credentials:

```txt
username: {room.uid}.{thing.uid}
password: {connectivity.password}
```

After authentication, devices are authorized to publish messages to the `telemetry/{room_uid}/{thing_uid}` MQTT topic.

## Room Events

This service listens to room deletion events on the Kafka topic for cleanup operations. The input topic is named
`uiot-system-events` by default. Only `room_delete` events are processed.

**Example room deletion event:**

```json
{
  "type": "room_delete",
  "room_delete": {
    "room_uid": "{room}"
  }
}
```
