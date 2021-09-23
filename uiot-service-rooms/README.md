# uIoT Service Rooms

Microservice for rooms management. Offers a single REST API endpoint.

## Configuration

| Environment variable                     | Description                                                              |
| ---------------------------------------- | ------------------------------------------------------------------------ |
| `SPRING_DATA_MONGODB_URI`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.            |
| `SPRING_DATA_MONGODB_DATABASE`           | MongoDB database, default: `uiot-service-rooms`.                         |
| `SPRING_KAFKA_STREAMS_APPLICATION_ID`    | Kafka Streams `application.id` property, default: `uiot-service-rooms`.  |
| `SPRING_KAFKA_STREAMS_BOOTSTRAP_SERVERS` | Comma-separated `bootstrap.servers` property, default: `localhost:9092`. |
| `UIOT_SYSTEM_EVENTS_TOPIC`               | Kafka topic for system events. See [here](#rooms-deletion-integration).  |

## REST API

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
