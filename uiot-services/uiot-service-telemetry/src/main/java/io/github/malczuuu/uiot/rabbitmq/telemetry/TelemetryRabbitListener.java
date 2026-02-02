package io.github.malczuuu.uiot.rabbitmq.telemetry;

import io.github.malczuuu.uiot.models.AccountingMetric;
import io.github.malczuuu.uiot.models.Pack;
import io.github.malczuuu.uiot.models.Record;
import io.github.malczuuu.uiot.models.ThingEvent;
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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
@RabbitListener(queues = {"${uiot.rabbitmq-input-queue}"})
public class TelemetryRabbitListener implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(TelemetryRabbitListener.class);

  private final DeviceEventKafkaSink deviceEventKafkaSink;
  private final AccountingKafkaSink accountingKafkaSink;
  private final JsonMapper jsonMapper;
  private final Clock clock;

  private final String routingKeyRegexp;

  private Pattern routingKeyPattern;

  public TelemetryRabbitListener(
      DeviceEventKafkaSink deviceEventKafkaSink,
      AccountingKafkaSink accountingKafkaSink,
      JsonMapper jsonMapper,
      Clock clock,
      @Value("${uiot.rabbitmq-routing-key-regexp}") String routingKeyRegexp) {
    this.deviceEventKafkaSink = deviceEventKafkaSink;
    this.accountingKafkaSink = accountingKafkaSink;
    this.jsonMapper = jsonMapper;
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
      pack = jsonMapper.readValue(message, Pack.class);
    } catch (JacksonException e) {
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

    if (!events.isEmpty()) {
      deviceEventKafkaSink.sink(events);
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
    accountingKafkaSink.sink(accounting);
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
