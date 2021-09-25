# uIoT Accounting Service

A microservice for resources usage accounting. It processes accounting streaming data and serves
HTTP API for it.

| Ports  | Description |
| ------ | ----------- |
| `8330` | HTTP API    |

Note that in Docker, HTTP API is served on `8080`.

## HTTP API

| Method   | Endpoint                       | Parameters                   |
| -------- | ------------------------------ | ---------------------------- |
| `GET`    | `/api/rooms/{room}/accounting` | `room_uid`, `since`, `until` |

## Kafka accounting messages

1. Message produced on raw accounting topic (`uiot-accounting` by default).

    ```json
    {"type":"mqtt_inbound","room_uid":"room1","value":12,"time":1231231,"tags":{"thing_uid":"thing1"}}
    ```

1. Message produced on time-windowed accounting topic (`uiot-accounting-windowed` by default).

    ```json
    {"room_uid":"room1","tags":{"thing_uid":"thing1"},"times":[1628939340,1628939400],"type":"mqtt_inbound","uuid":"f9612419-d3f4-4a4b-a2cd-5aa61ccb3159","value":12}
    ```
