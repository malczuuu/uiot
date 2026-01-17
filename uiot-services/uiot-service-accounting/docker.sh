#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-accounting:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-accounting/Dockerfile \
  uiot-services/uiot-service-accounting
