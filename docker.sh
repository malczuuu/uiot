#!/bin/sh

./gradlew clean build -x test

./uiot-service-connectivity/docker.sh && \
./uiot-service-history/docker.sh && \
./uiot-service-telemetry/docker.sh && \
./uiot-service-rooms/docker.sh && \
./uiot-service-things/docker.sh
