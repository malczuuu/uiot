#!/bin/sh

kafka-topics.sh --create --if-not-exists --topic uiot-accounting --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092
kafka-topics.sh --create --if-not-exists --topic uiot-accounting-windows --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092

kafka-topics.sh --create --if-not-exists --topic uiot-thing-events --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092

kafka-topics.sh --create --if-not-exists --topic uiot-action-execution-events --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092

kafka-topics.sh --create --if-not-exists --topic uiot-system-events --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092

kafka-topics.sh --create --if-not-exists --topic uiot-service-history_thing-metadata --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092
kafka-topics.sh --create --if-not-exists --topic uiot-service-history_keyed-thing-events --replication-factor=1 --partitions=5 --bootstrap-server kafka:29092
