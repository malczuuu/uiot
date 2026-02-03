package io.github.malczuuu.uiot.room.core;

import io.github.malczuuu.uiot.models.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class RoomEventBrokerImpl implements RoomEventBroker {

  private static final Logger log = LoggerFactory.getLogger(RoomEventBrokerImpl.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final JsonMapper jsonMapper;

  private final String systemEventsTopic;

  public RoomEventBrokerImpl(
      KafkaOperations<String, String> kafkaOperations,
      JsonMapper jsonMapper,
      @Value("${uiot.system-events-topic}") String systemEventsTopic) {
    this.kafkaOperations = kafkaOperations;
    this.jsonMapper = jsonMapper;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void publish(RoomCreateEvent event) {
    RoomCreateEnvelope envelope = new RoomCreateEnvelope(event);
    String json = jsonMapper.writeValueAsString(envelope);

    kafkaOperations.send(systemEventsTopic, event.getRoomUid(), json);

    log.info("Published type={} event with room={}", RoomCreateEnvelope.TYPE, event.getRoomUid());
  }

  @Override
  public void publish(RoomDeleteEvent event) {
    RoomDeleteEnvelope envelope = new RoomDeleteEnvelope(event);
    String json = jsonMapper.writeValueAsString(envelope);

    kafkaOperations.send(systemEventsTopic, event.getRoomUid(), json);

    log.info(
        "Published type={} event with room_uid={}", RoomDeleteEnvelope.TYPE, event.getRoomUid());
  }
}
