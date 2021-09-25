# uIoT Service History

A microservice, which processes telemetry data and stores it into MongoDB database. Also serves
historical and last-state REST API endpoints.

| Ports  | Description |
| ------ | ----------- |
| `8332` | HTTP API    |

Note that in Docker, HTTP API is served on `8080`.

## REST API

History records are grouped within rooms, which are managed by different service. Last-state records
involve single thing only.

| Method | Endpoint                                              | Parameters  |
| ------ | ----------------------------------------------------- | ----------- |
| `GET`  | `/api/rooms/{room}/history`                           | `thing_uid` |
| `GET`  | `/api/rooms/{room}/things/{thing}/last-state`         |             |

## History stream processing

Service consumes messages from telemetry stream (`uiot-telemetry` topic by default) and stores it
within MongoDB. Also, service builds KTable involving each device's last state.
