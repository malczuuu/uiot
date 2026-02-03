package io.github.malczuuu.uiot.testkit.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MongoContainerConfiguration {

  @Bean
  @ServiceConnection
  public MongoDBContainer mongoContainer() {
    return new MongoDBContainer("mongo:8.2.3");
  }
}
