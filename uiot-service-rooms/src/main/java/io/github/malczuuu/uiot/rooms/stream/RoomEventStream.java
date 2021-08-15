package io.github.malczuuu.uiot.rooms.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.room.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.room.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.rooms.core.RoomService;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class RoomEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(RoomEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;
  private final RoomService roomService;

  private final String systemEventsTopic;

  public RoomEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      RoomService roomService,
      @Value("${uiot.rooms.kafka-system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.roomService = roomService;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    Consumed<String, String> consumed =
        Consumed.with(
            Serdes.String(),
            Serdes.String(),
            new WallclockTimestampExtractor(),
            AutoOffsetReset.LATEST);

    streamsBuilder.stream(systemEventsTopic, consumed)
        .foreach((key, value) -> processSystemEvent(value));
  }

  private void processSystemEvent(String value) {
    try {
      processSystemEventInternal(value);
    } catch (JsonProcessingException ignored) {
    }
  }

  private void processSystemEventInternal(String value) throws JsonProcessingException {
    JsonNode node = objectMapper.readTree(value);
    String type = node.get("type").asText("");
    switch (type) {
      case RoomCreateEnvelope.TYPE:
        triggerRoomCreation(node);
        break;
      case RoomDeleteEnvelope.TYPE:
        triggerRoomDeletion(node);
    }
  }

  private void triggerRoomCreation(JsonNode node) throws JsonProcessingException {
    RoomCreateEnvelope envelope = objectMapper.treeToValue(node, RoomCreateEnvelope.class);

    roomService.createRoom(envelope.getRoomCreateEvent());

    log.info(
        "Triggered room creation via streams processing, room={}, timestamp={}",
        envelope.getRoomCreateEvent().getRoomUid(),
        envelope.getRoomCreateEvent().getTime());
  }

  private void triggerRoomDeletion(JsonNode node) throws JsonProcessingException {
    RoomDeleteEnvelope envelope = objectMapper.treeToValue(node, RoomDeleteEnvelope.class);

    roomService.deleteRoom(envelope.getRoomDeleteEvent().getRoomUid());

    log.info(
        "Triggered room deletion via streams processing, room_uid={}, timestamp={}",
        envelope.getRoomDeleteEvent().getRoomUid(),
        envelope.getRoomDeleteEvent().getTime());
  }
}
