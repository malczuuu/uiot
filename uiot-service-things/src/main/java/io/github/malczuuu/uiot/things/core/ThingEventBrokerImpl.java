package io.github.malczuuu.uiot.things.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.schema.event.thing.ThingCreateEnvelope;
import io.github.malczuuu.uiot.schema.event.thing.ThingCreateEvent;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEnvelope;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

@Service
public class ThingEventBrokerImpl implements ThingEventBroker {

  private static final Logger log = LoggerFactory.getLogger(ThingEventBrokerImpl.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final ObjectMapper objectMapper;

  private final String systemEventsTopic;

  public ThingEventBrokerImpl(
      KafkaOperations<String, String> kafkaOperations,
      ObjectMapper objectMapper,
      @Value("${uiot.things.kafka-system-events-topic}") String systemEventsTopic) {
    this.kafkaOperations = kafkaOperations;
    this.objectMapper = objectMapper;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void publish(ThingDeleteEvent event) {
    ThingDeleteEnvelope envelope = new ThingDeleteEnvelope(event);
    String json;
    try {
      json = objectMapper.writeValueAsString(envelope);
    } catch (JsonProcessingException e) {
      return;
    }

    kafkaOperations.send(systemEventsTopic, event.getRoomUid(), json);

    log.info(
        "Published type={} event with roomUid={}, thingUid={} and timestamp={}",
        ThingDeleteEnvelope.TYPE,
        event.getRoomUid(),
        event.getThingUid(),
        event.getTimestamp());
  }

  @Override
  public void publish(ThingCreateEvent event) {
    ThingCreateEnvelope envelope = new ThingCreateEnvelope(event);
    String json;
    try {
      json = objectMapper.writeValueAsString(envelope);
    } catch (JsonProcessingException e) {
      return;
    }

    kafkaOperations.send(systemEventsTopic, event.getRoomUid(), json);

    log.info(
        "Published type={} event with roomUid={}, thingUid={} and timestamp={}",
        ThingDeleteEnvelope.TYPE,
        event.getRoomUid(),
        event.getThingUid(),
        event.getTimestamp());
  }
}
