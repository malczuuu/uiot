#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-telemetry:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-telemetry/Dockerfile \
  uiot-services/uiot-service-telemetry
