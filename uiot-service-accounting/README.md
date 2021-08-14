# uIoT Accounting Service

Service for resources usage accounting. It processes accounting streaming data and serves HTTP API
for it.

Serves HTTP API on port `8330` (`8080` within Docker container).

## Kafka Messages

1. Message produced on raw accounting topic (`uiot-accounting`).

    ```
    {"type":"mqtt_inbound","room_uid":"room1","value":12,"time":1231231,"tags":{"thing_uid":"thing1"}}
    ```

1. Message produced on time-windowed accounting topic (`uiot-accounting-windowed`).

    ```
    {"room_uid":"room1","tags":{"thing_uid":"thing1"},"times":[1628939340,1628939400],"type":"mqtt_inbound","uuid":"f9612419-d3f4-4a4b-a2cd-5aa61ccb3159","value":12}
    ```
