#!/bin/sh

./gradlew clean build -x test

./uiot-services/uiot-service-accounting/docker.sh && \
./uiot-services/uiot-service-connectivity/docker.sh && \
./uiot-services/uiot-service-history/docker.sh && \
./uiot-services/uiot-service-rooms/docker.sh && \
./uiot-services/uiot-service-rules/docker.sh && \
./uiot-services/uiot-service-telemetry/docker.sh && \
./uiot-services/uiot-service-things/docker.sh
