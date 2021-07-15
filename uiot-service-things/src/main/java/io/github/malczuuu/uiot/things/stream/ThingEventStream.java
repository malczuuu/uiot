package io.github.malczuuu.uiot.things.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.schema.event.thing.ThingCreateEnvelope;
import io.github.malczuuu.uiot.schema.event.thing.ThingCreateEvent;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEnvelope;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEvent;
import io.github.malczuuu.uiot.things.core.ThingService;
import io.github.malczuuu.uiot.things.model.ThingModel;
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
public class ThingEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(ThingEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;
  private final ThingService thingService;

  private final String systemEventsTopic;

  public ThingEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      ThingService thingService,
      @Value("${uiot.things.kafka-system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.thingService = thingService;
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

    streamsBuilder.stream(systemEventsTopic, consumed).foreach(this::processSystemEvent);
  }

  private void processSystemEvent(String key, String value) {
    try {
      processSystemEventInternal(key, value);
    } catch (JsonProcessingException ignored) {
    }
  }

  private void processSystemEventInternal(String key, String value) throws JsonProcessingException {
    log.debug("Received an event=({}, {}) on Kafka topic={}", key, value, systemEventsTopic);

    JsonNode node = objectMapper.readTree(value);
    String type = node.get("type").asText("");
    switch (type) {
      case ThingCreateEnvelope.TYPE:
        triggerThingCreation(node);
        break;
      case ThingDeleteEnvelope.TYPE:
        triggerThingDeletion(node);
    }
  }

  private void triggerThingCreation(JsonNode node) throws JsonProcessingException {
    ThingCreateEnvelope envelope = objectMapper.treeToValue(node, ThingCreateEnvelope.class);
    ThingCreateEvent event = envelope.getCreateThingEvent();

    thingService.createThing(
        event.getRoomUid(), new ThingModel(event.getThingUid(), event.getThingName(), 0L));

    log.info(
        "Triggered thing creation via streams processing, thing={}, timestamp={}",
        event.getThingUid(),
        event.getTimestamp());
  }

  private void triggerThingDeletion(JsonNode node) throws JsonProcessingException {
    ThingDeleteEnvelope envelope = objectMapper.treeToValue(node, ThingDeleteEnvelope.class);
    ThingDeleteEvent event = envelope.getDeleteThingEvent();

    thingService.deleteThing(event.getRoomUid(), event.getThingUid());

    log.info(
        "Triggered thing deletion via streams processing, thing={}, timestamp={}",
        event.getThingUid(),
        event.getTimestamp());
  }
}
