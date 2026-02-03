package io.github.malczuuu.uiot.testkit.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.RabbitMQContainer;

@TestConfiguration(proxyBeanMethods = false)
public class RabbitContainerConfiguration {

  @Bean
  @ServiceConnection
  public RabbitMQContainer rabbitContainer() {
    return new RabbitMQContainer("rabbitmq:4.2.2-management-alpine");
  }
}
