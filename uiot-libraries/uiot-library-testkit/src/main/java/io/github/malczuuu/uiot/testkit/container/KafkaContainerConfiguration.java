package io.github.malczuuu.uiot.testkit.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class KafkaContainerConfiguration {

  @Bean
  @ServiceConnection
  public KafkaContainer kafkaContainer() {
    return new KafkaContainer(DockerImageName.parse("apache/kafka:4.1.1"))
        .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
        .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1")
        .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1");
  }

  @Bean
  public KafkaAdmin.NewTopics uiotTopics() {
    return new KafkaAdmin.NewTopics(
        TopicBuilder.name("uiot-accounting").partitions(5).replicas(1).build(),
        TopicBuilder.name("uiot-accounting-windows").partitions(5).replicas(1).build(),
        TopicBuilder.name("uiot-thing-events").partitions(5).replicas(1).build(),
        TopicBuilder.name("uiot-action-execution-events").partitions(5).replicas(1).build(),
        TopicBuilder.name("uiot-system-events").partitions(5).replicas(1).build(),
        TopicBuilder.name("uiot-service-history_thing-metadata").partitions(5).replicas(1).build(),
        TopicBuilder.name("uiot-service-history_keyed-thing-events")
            .partitions(5)
            .replicas(1)
            .build());
  }
}
