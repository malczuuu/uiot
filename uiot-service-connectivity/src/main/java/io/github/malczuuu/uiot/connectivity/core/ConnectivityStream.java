package io.github.malczuuu.uiot.connectivity.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class ConnectivityStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(ConnectivityStream.class);

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;

  private final ConnectivityRepository connectivityRepository;

  private final String systemEventsTopic;

  public ConnectivityStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      ConnectivityRepository connectivityRepository,
      @Value("${uiot.rmq-auth.kafka-system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.connectivityRepository = connectivityRepository;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    setupInternalEventsStream();
  }

  private void setupInternalEventsStream() {
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
    if (node.get("type").asText("").equals("deleteThingEvent")) {
      ThingDeleteEnvelope envelope = objectMapper.treeToValue(node, ThingDeleteEnvelope.class);
      ThingDeleteEvent event = envelope.getDeleteThingEvent();

      connectivityRepository.deleteByRoomAndThing(event.getRoomUid(), event.getThingUid());

      log.debug(
          "Successfully deleted events for thing={}, deletion requested at timestamp={}",
          event.getThingUid(),
          event.getTimestamp());
    } else {
      String type = node.get("type").asText("");
      log.debug("Ignoring internal event of type={}, value={}", type, node.get("type"));
    }
  }
}
