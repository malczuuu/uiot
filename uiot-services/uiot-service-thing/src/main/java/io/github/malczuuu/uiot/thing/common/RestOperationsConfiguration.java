package io.github.malczuuu.uiot.thing.common;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
public class RestOperationsConfiguration {

  @Bean
  public RestOperations restOperations(RestTemplateBuilder builder) {
    return builder.build();
  }
}
