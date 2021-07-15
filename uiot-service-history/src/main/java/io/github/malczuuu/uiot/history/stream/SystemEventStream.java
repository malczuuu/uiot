package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.history.core.HistoryService;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEnvelope;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEvent;
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
  private final HistoryService historyService;
  private final ObjectMapper objectMapper;

  private final TopicProperties topics;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      HistoryService historyService,
      ObjectMapper objectMapper,
      TopicProperties topics) {
    this.streamsBuilder = streamsBuilder;
    this.historyService = historyService;
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
    if (node.get("type").asText("").equals(ThingDeleteEnvelope.TYPE)) {
      ThingDeleteEnvelope envelope = objectMapper.treeToValue(node, ThingDeleteEnvelope.class);
      ThingDeleteEvent event = envelope.getDeleteThingEvent();

      historyService.registerThingDeletion(
          event.getRoomUid(), event.getThingUid(), event.getTimestamp());

      log.debug(
          "Successfully deleted events for room_uid={}, thing_uid={}, deletion requested at timestamp={}",
          event.getRoomUid(),
          event.getThingUid(),
          event.getTimestamp());
    } else {
      String type = node.get("type").asText("");
      log.debug("Ignoring internal event of type={}, value={}", type, node.get("type"));
    }
  }
}
