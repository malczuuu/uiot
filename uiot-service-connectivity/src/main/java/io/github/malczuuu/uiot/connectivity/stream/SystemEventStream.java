package io.github.malczuuu.uiot.things.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.room.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.room.RoomDeleteEvent;
import io.github.malczuuu.uiot.things.core.ThingService;
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
public class SystemEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(SystemEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;

  private final ThingService thingService;

  private final String systemEventsTopic;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      ThingService thingService,
      @Value("${uiot.things.system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.thingService = thingService;
    this.systemEventsTopic = systemEventsTopic;
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
    if (RoomDeleteEnvelope.TYPE.equals(type)) {
      deleteThings(node);
    } else {
      log.debug("Ignoring internal event of type={}, value={}", type, node.get("type"));
    }
  }

  private void deleteThings(JsonNode node) throws JsonProcessingException {
    RoomDeleteEnvelope envelope = objectMapper.treeToValue(node, RoomDeleteEnvelope.class);
    RoomDeleteEvent event = envelope.getRoomDeleteEvent();
    thingService.deleteThings(event.getRoomUid());
  }
}
