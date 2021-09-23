# uIoT

A prototype of an IoT Telemetry system, presenting microservices communication over Apache Kafka and
REST API. Implemented as a part of the master's thesis _Analysis of selected communication
techniques in systems based on microservices architecture_ at
[Cracow University of Technology](https://pk.edu.pl) by Damian Malczewski in 2021.

See `README.md` files of each service repository for more details.

## Running in Docker environment

1. Create `uiot` network.

   ```bash
   $ docker network uiot create
   ```

2. Launch environment services of MongoDB, RabbitMQ and Kafka.

   ```bash
   $ cd uiot-docker-environment/
   $ docker-compose up -d
   ```

   Setting up can take a while. You can verify if everything is up and running.

   ```bash
   $ docker-compose ps
   ```

3. Prepare Kafka topics.

   ```bash
   $ ./kafka-topics.sh
   ```

5. After everything works fine, start application services.

   ```bash
   $ cd uiot-docker-services/
   $ docker-compose up -d
   ```
