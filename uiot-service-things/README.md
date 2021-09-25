# uIoT Service Things

A microservice for things management. Offers a single REST API endpoint.

| Ports  | Description |
| ------ | ----------- |
| `8336` | HTTP API    |

Note that in Docker, HTTP API is served on `8080`.

## Configuration

| Environment variable                     | Description                                                                            |
| ---------------------------------------- | -------------------------------------------------------------------------------------- |
| `SPRING_DATA_MONGODB_URI`                | MongoDB connection URI, default: `mongodb://127.0.0.1:27017`.                          |
| `SPRING_DATA_MONGODB_DATABASE`           | MongoDB database, default: `uiot-service-things`.                                      |
| `SPRING_KAFKA_STREAMS_APPLICATION_ID`    | Kafka Streams `application.id` property, default: `uiot-service-things`.               |
| `SPRING_KAFKA_STREAMS_BOOTSTRAP_SERVERS` | Comma-separated `bootstrap.servers` property, default: `localhost:9092`.               |
| `UIOT_CONNECTIVITY_INTEGRATION_URL`      | Integration URL for connectivity management. See [here](#things-deletion-integration). |
| `UIOT_SYSTEM_EVENTS_TOPIC`               | Kafka topic for system events. See [here](#rooms-deletion-integration).                |

## REST API

Things are grouped within rooms, which are managed by different service.

| Method   | Endpoint                           |
| -------- | ---------------------------------- |
| `GET`    | `/api/rooms/{room}/things`         |
| `POST`   | `/api/rooms/{room}/things`         |
| `GET`    | `/api/rooms/{room}/things/{thing}` |
| `PUT`    | `/api/rooms/{room}/things/{thing}` |
| `DELETE` | `/api/rooms/{room}/things/{thing}` |

## Things deletion integration

This service requests deletion in `uiot-service-connectivity` directly via REST API. URL for such
integration is configurable via `UIOT_CONNECTIVITY_INTEGRATION_URL`. By default, it points
to `http://localhost:8333/api/rooms/{roomUid}/things/{thingUid}/connectivity`

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
