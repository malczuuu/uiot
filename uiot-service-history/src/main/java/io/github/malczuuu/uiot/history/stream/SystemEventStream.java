package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.history.core.StorageService;
import io.github.malczuuu.uiot.models.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class SystemEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(SystemEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final StorageService storageService;
  private final ObjectMapper objectMapper;
  private final TopicProperties topics;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      StorageService storageService,
      ObjectMapper objectMapper,
      TopicProperties topics) {
    this.streamsBuilder = streamsBuilder;
    this.storageService = storageService;
    this.objectMapper = objectMapper;
    this.topics = topics;
  }

  @Override
  public void afterPropertiesSet() {
    setupSystemEventStream();
  }

  private void setupSystemEventStream() {
    Consumed<String, String> consumed =
        Consumed.with(
            Serdes.String(),
            Serdes.String(),
            new WallclockTimestampExtractor(),
            AutoOffsetReset.LATEST);
    streamsBuilder.stream(topics.getSystemEventsTopic(), consumed)
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
        createStorage(node);
        break;
      case RoomDeleteEnvelope.TYPE:
        deleteStorage(node);
        break;
      default:
        log.debug("Ignoring internal event of type={}, value={}", type, node.get("type"));
    }
  }

  private void createStorage(JsonNode node) throws JsonProcessingException {
    RoomCreateEnvelope envelope = objectMapper.treeToValue(node, RoomCreateEnvelope.class);
    RoomCreateEvent event = envelope.getRoomCreateEvent();
    storageService.createStorage(event);
  }

  private void deleteStorage(JsonNode node) throws JsonProcessingException {
    RoomDeleteEnvelope envelope = objectMapper.treeToValue(node, RoomDeleteEnvelope.class);
    RoomDeleteEvent event = envelope.getRoomDeleteEvent();
    storageService.deleteStorage(event);
  }
}
