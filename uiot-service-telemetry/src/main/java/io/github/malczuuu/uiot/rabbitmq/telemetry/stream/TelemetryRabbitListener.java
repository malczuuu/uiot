package io.github.malczuuu.uiot.rabbitmq.telemetry.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.accounting.AccountingMetric;
import io.github.malczuuu.uiot.models.telemetry.Pack;
import io.github.malczuuu.uiot.models.telemetry.Record;
import io.github.malczuuu.uiot.models.thing.ThingEvent;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {"${uiot.telemetry.rabbitmq-input-queue}"})
public class TelemetryRabbitListener implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(TelemetryRabbitListener.class);

  private final DeviceEventKafkaService deviceEventKafkaService;
  private final AccountingKafkaService accountingKafkaService;
  private final ObjectMapper objectMapper;
  private final Clock clock;

  private final String routingKeyRegexp;

  private Pattern routingKeyPattern;

  public TelemetryRabbitListener(
      DeviceEventKafkaService deviceEventKafkaService,
      AccountingKafkaService accountingKafkaService,
      ObjectMapper objectMapper,
      Clock clock,
      @Value("${uiot.telemetry.rabbitmq-routing-key-regexp}") String routingKeyRegexp) {
    this.deviceEventKafkaService = deviceEventKafkaService;
    this.accountingKafkaService = accountingKafkaService;
    this.objectMapper = objectMapper;
    this.clock = clock;
    this.routingKeyRegexp = routingKeyRegexp;
  }

  @Override
  public void afterPropertiesSet() {
    routingKeyPattern = Pattern.compile(routingKeyRegexp);
  }

  @RabbitHandler
  public void receive(
      @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, @Payload byte[] message) {
    Pack pack;
    try {
      pack = objectMapper.readValue(message, Pack.class);
    } catch (IOException e) {
      log.info(
          "Received message is not a valid pack, routingKey={}, payload={}",
          routingKey,
          Base64.getEncoder().encodeToString(message));
      return;
    }

    if (pack.isEmpty()) {
      log.debug(
          "Received message is an empty pack, routingKey={}, payload={}",
          routingKey,
          Base64.getEncoder().encodeToString(message));
      return;
    }

    Instant arrivalTime = clock.instant();

    Matcher matcher = routingKeyPattern.matcher(routingKey);
    if (!matcher.find()) {
      log.error(
          "Received message on routingKey={} which does not match pattern={}",
          routingKey,
          routingKeyRegexp);
      return;
    }

    String room = matcher.group(1);
    String thing = matcher.group(2);

    List<ThingEvent> events =
        pack.stream()
            .flatMap(record -> toDeviceEvent(record, arrivalTime).stream())
            .filter(e -> e.getRoom().equals(room) && e.getThing().equals(thing))
            .collect(Collectors.toList());

    if (events.size() > 0) {
      deviceEventKafkaService.sink(events);
      log.info(
          "Successfully processed message on routingKey={}, payload={}",
          routingKey,
          Base64.getEncoder().encodeToString(message));
    } else {
      log.info(
          "Processed message resulted with an empty pack, routingKey={}, payload={}",
          routingKey,
          Base64.getEncoder().encodeToString(message));
    }

    AccountingMetric accounting = toAccountingMetric(message, arrivalTime, room, thing);
    accountingKafkaService.sink(accounting);
  }

  private AccountingMetric toAccountingMetric(
      byte[] message, Instant arrivalTime, String room, String thing) {
    Map<String, String> tags = new HashMap<>();
    tags.put("thing_uid", thing);
    return new AccountingMetric(
        UUID.randomUUID().toString(),
        "inbound_mqtt",
        room,
        (double) message.length,
        arrivalTime.getEpochSecond() * 1000_000_000L + arrivalTime.getNano(),
        tags);
  }

  private Optional<ThingEvent> toDeviceEvent(Record record, Instant arrivalTime) {
    String name = record.getName();
    String[] split = name.split(":", 6);
    if (split.length != 6) {
      return Optional.empty();
    }

    String id = UUID.randomUUID().toString();
    String room = split[3];
    String device = split[4];
    String property = split[5];

    long arrivalTimeInNanos = arrivalTime.getEpochSecond() * 1000_000_000L + arrivalTime.getNano();

    return Optional.of(ThingEvent.from(record, id, room, device, property, arrivalTimeInNanos));
  }
}
