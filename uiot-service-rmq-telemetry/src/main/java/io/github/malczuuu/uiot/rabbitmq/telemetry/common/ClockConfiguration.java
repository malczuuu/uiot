package io.github.malczuuu.uiot.rabbitmq.telemetry.common;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
