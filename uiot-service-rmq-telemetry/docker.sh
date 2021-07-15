#!/bin/sh

docker build \
  -t uiot-service-rmq-telemetry:latest \
  -f uiot-service-rmq-telemetry/Dockerfile \
  uiot-service-rmq-telemetry/build/libs
