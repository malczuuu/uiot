#!/bin/sh

./gradlew clean build -x test

./uiot-services/uiot-service-accounting/docker.sh && \
./uiot-services/uiot-service-connectivity/docker.sh && \
./uiot-services/uiot-service-history/docker.sh && \
./uiot-services/uiot-service-room/docker.sh && \
./uiot-services/uiot-service-rule/docker.sh && \
./uiot-services/uiot-service-telemetry/docker.sh && \
./uiot-services/uiot-service-thing/docker.sh
