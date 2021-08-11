package io.github.malczuuu.uiot.rooms.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.room.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.room.RoomCreateEvent;
import io.github.malczuuu.uiot.models.room.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.room.RoomDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

@Service
public class RoomEventBrokerImpl implements RoomEventBroker {

  private static final Logger log = LoggerFactory.getLogger(RoomEventBrokerImpl.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final ObjectMapper objectMapper;

  private final String systemEventsTopic;

  public RoomEventBrokerImpl(
      KafkaOperations<String, String> kafkaOperations,
      ObjectMapper objectMapper,
      @Value("${uiot.rooms.kafka-system-events-topic}") String systemEventsTopic) {
    this.kafkaOperations = kafkaOperations;
    this.objectMapper = objectMapper;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void publish(RoomCreateEvent event) {
    RoomCreateEnvelope envelope = new RoomCreateEnvelope(event);
    String json;
    try {
      json = objectMapper.writeValueAsString(envelope);
    } catch (JsonProcessingException e) {
      return;
    }

    kafkaOperations.send(systemEventsTopic, event.getRoomUid(), json);

    log.info(
        "Published type={} event with room={} and timestamp={}",
        RoomCreateEnvelope.TYPE,
        event.getRoomUid(),
        event.getTimestamp());
  }

  @Override
  public void publish(RoomDeleteEvent event) {
    RoomDeleteEnvelope envelope = new RoomDeleteEnvelope(event);
    String json;
    try {
      json = objectMapper.writeValueAsString(envelope);
    } catch (JsonProcessingException e) {
      return;
    }

    kafkaOperations.send(systemEventsTopic, event.getRoomUid(), json);

    log.info(
        "Published type={} event with room_uid={} and timestamp={}",
        RoomDeleteEnvelope.TYPE,
        event.getRoomUid(),
        event.getTimestamp());
  }
}
