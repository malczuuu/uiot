# uIoT Service Rooms

A microservice for rooms management. Offers a single REST API endpoint.

| Ports  | Description |
| ------ | ----------- |
| `8333` | HTTP API    |

Note that in Docker, HTTP API is served on `8080`.

## Configuration

| Environment variable                     | Description                                                              |
| ---------------------------------------- | ------------------------------------------------------------------------ |
| `SPRING_DATA_MONGODB_URI`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.            |
| `SPRING_DATA_MONGODB_DATABASE`           | MongoDB database, default: `uiot-service-rooms`.                         |
| `SPRING_KAFKA_STREAMS_APPLICATION_ID`    | Kafka Streams `application.id` property, default: `uiot-service-rooms`.  |
| `SPRING_KAFKA_STREAMS_BOOTSTRAP_SERVERS` | Comma-separated `bootstrap.servers` property, default: `localhost:9092`. |
| `UIOT_SYSTEM_EVENTS_TOPIC`               | Kafka topic for system events. See [here](#rooms-deletion-integration).  |

## REST API

Rooms are top-level hierarchy in uIoT system.

| Method   | Endpoint             |
| -------- | -------------------- |
| `GET`    | `/api/rooms`         |
| `POST`   | `/api/rooms`         |
| `GET`    | `/api/rooms/{room}`  |
| `PUT`    | `/api/rooms/{room}`  |
| `DELETE` | `/api/rooms/{room}`  |

## Rooms deletion integration

This service publishes room deletion events on Kafka topic. Such topic is named `uiot-system-events`
by default. The expected model is following.

```json
{
  "type": "room_delete",
  "room_delete": {
    "room_uid": "{room}"
  }
}
```
