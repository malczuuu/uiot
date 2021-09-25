# uIoT Service Rooms

A microservice for rooms management. Offers REST API endpoints for connectivity management and for
RabbitMQ integration.

| Ports  | Description |
| ------ | ----------- |
| `8331` | HTTP API    |

Note that in Docker, HTTP API is served on `8080`.

## Configuration

| Environment variable                     | Description                                                                    |
| ---------------------------------------- | ------------------------------------------------------------------------------ |
| `SPRING_DATA_MONGODB_URI`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.                  |
| `SPRING_DATA_MONGODB_DATABASE`           | MongoDB database, default: `uiot-service-connectivity`.                        |
| `SPRING_KAFKA_STREAMS_APPLICATION_ID`    | Kafka Streams `application.id` property, default: `uiot-service-connectivity`. |
| `SPRING_KAFKA_STREAMS_BOOTSTRAP_SERVERS` | Comma-separated `bootstrap.servers` property, default: `localhost:9092`.       |
| `UIOT_VHOST`                             | RabbitMQ global vhost for the things authorization, default: `/`.              |
| `UIOT_ROUTING_KEY_TEMPLATE`              | RabbitMQ telemetry routing key template, default: `telemetry.%s.%s`.           |
| `UIOT_USERNAME_CONTEXT_SEPARATOR`        | RabbitMQ username context separator, default: `\.`.                            |
| `UIOT_SYSTEM_EVENTS_TOPIC`               | Kafka topic for system events. See [here](#rooms-deletion-integration).        |

## REST API

Connectivity information are grouped within rooms, which are managed by different service. Each
connectivity corresponds to a single thing.

| Method   | Endpoint                                        |
| -------- | ----------------------------------------------- |
| `GET`    | `/api/rooms/{room}/things/{thing}/connectivity` |
| `POST`   | `/api/rooms/{room}/things/{thing}/connectivity` |
| `PUT`    | `/api/rooms/{room}/things/{thing}/connectivity` |
| `DELETE` | `/api/rooms/{room}/things/{thing}/connectivity` |

## REST API for RabbitMQ integration

| Method   | Endpoint         |
| -------- | ---------------- |
| `POST`   | `/auth/user`     |
| `POST`   | `/auth/vhost`    |
| `POST`   | `/auth/resource` |
| `POST`   | `/auth/topic`    |

## Rooms deletion integration

This service accepts also room deletion events on Kafka topic. Input topic for this purpose is
named `uiot-system-events` by default. The expected model is following.

```json
{
  "type": "room_delete",
  "room_delete": {
    "room_uid": "{room}"
  }
}
```
