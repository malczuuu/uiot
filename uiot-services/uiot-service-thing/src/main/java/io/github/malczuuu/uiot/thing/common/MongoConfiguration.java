package io.github.malczuuu.uiot.thing.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
public class MongoConfiguration {

  @Bean
  public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTransactionManager(mongoDatabaseFactory);
  }
}
