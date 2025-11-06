package io.github.malczuuu.uiot.rabbitmq.telemetry;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class RabbitTelemetryApplication {

  public static void main(String[] args) {
    SpringApplication.run(RabbitTelemetryApplication.class, args);
  }
}
