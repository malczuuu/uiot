package io.github.malczuuu.uiot.rabbitmq.telemetry.common;

import java.util.HashMap;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

  private final String exchange = "amq.topic";
  private final String routingKey;
  private final String inputQueue;

  public RabbitConfiguration(
      @Value("${uiot.rabbitmq-routing-key}") String routingKey,
      @Value("${uiot.rabbitmq-input-queue}") String inputQueue) {
    this.routingKey = routingKey;
    this.inputQueue = inputQueue;
  }

  @Bean
  public Exchange exchange() {
    return new TopicExchange(exchange, true, false);
  }

  @Bean
  public Queue queue() {
    return new Queue(inputQueue, true, false, false);
  }

  @Bean
  public Binding binding() {
    return new Binding(inputQueue, DestinationType.QUEUE, exchange, routingKey, new HashMap<>());
  }
}
