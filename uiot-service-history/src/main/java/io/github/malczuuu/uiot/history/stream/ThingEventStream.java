package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.history.core.HistoryService;
import io.github.malczuuu.uiot.models.thing.ThingEvent;
import io.github.malczuuu.uiot.models.thing.ThingEventEnvelope;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class ThingEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(ThingEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final HistoryService historyService;
  private final ObjectMapper objectMapper;

  private final TopicProperties topics;

  public ThingEventStream(
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
    KStream<String, ThingEventEnvelope> thingEventStream = initiateThingEventStream();
    storeThingEventsInDatabase(thingEventStream);
    storeThingMetadataInKTable(thingEventStream);
    storePropertyLastStateInKTable(thingEventStream);
  }

  private KStream<String, ThingEventEnvelope> initiateThingEventStream() {
    return streamsBuilder.stream(
        topics.getThingEventsTopic(),
        Consumed.with(
            Serdes.String(),
            newBasicJsonSerde(ThingEventEnvelope.class),
            new WallclockTimestampExtractor(),
            AutoOffsetReset.LATEST));
  }

  private void storeThingEventsInDatabase(KStream<String, ThingEventEnvelope> thingEventStream) {
    thingEventStream.foreach((key, value) -> processThingEvent(value));
  }

  private void storeThingMetadataInKTable(KStream<String, ThingEventEnvelope> thingEventStream) {
    thingEventStream
        .flatMap((key, value) -> splitByThingKey(value))
        .groupByKey(Grouped.with(Serdes.String(), newBasicJsonSerde(ThingEvent.class)))
        .aggregate(
            ThingInfo::new,
            (key, value, aggregate) -> aggregateProperties(value, aggregate),
            Materialized.with(Serdes.String(), newBasicJsonSerde(ThingInfo.class)))
        .toStream()
        .to(
            topics.getThingMetadataTopic(),
            Produced.with(Serdes.String(), newBasicJsonSerde(ThingInfo.class)));

    streamsBuilder.globalTable(
        topics.getThingMetadataTopic(),
        Consumed.with(Serdes.String(), newBasicJsonSerde(ThingInfo.class)),
        Materialized.as(topics.getThingMetadataTopic()));
  }

  private void storePropertyLastStateInKTable(
      KStream<String, ThingEventEnvelope> thingEventStream) {
    thingEventStream
        .flatMap((key, value) -> splitByPropertyKey(value))
        .to(
            topics.getKeyedThingEventsTopic(),
            Produced.with(Serdes.String(), newBasicJsonSerde(ThingEvent.class)));

    streamsBuilder.globalTable(
        topics.getKeyedThingEventsTopic(),
        Consumed.with(Serdes.String(), newBasicJsonSerde(ThingEvent.class)),
        Materialized.as(topics.getKeyedThingEventsTopic()));
  }

  private <T> JsonSerde<T> newBasicJsonSerde(Class<T> clazz) {
    return new JsonSerde<>(clazz, objectMapper).ignoreTypeHeaders().noTypeInfo();
  }

  private void processThingEvent(ThingEventEnvelope value) {
    if (value.getThingEvents() != null) {
      value.getThingEvents().forEach(historyService::storeEvent);
    }
  }

  private Iterable<KeyValue<String, ThingEvent>> splitByThingKey(ThingEventEnvelope value) {
    if (value.getThingEvents() != null) {
      return value.getThingEvents().stream()
          .map(event -> new KeyValue<>(getThingKey(event), event))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private Iterable<KeyValue<String, ThingEvent>> splitByPropertyKey(ThingEventEnvelope value) {
    if (value.getThingEvents() != null) {
      return value.getThingEvents().stream()
          .map(event -> new KeyValue<>(getPropertyKey(event), event))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private String getThingKey(ThingEvent event) {
    return event.getRoom() + "::" + event.getThing();
  }

  private String getPropertyKey(ThingEvent event) {
    return event.getRoom() + "::" + event.getThing() + "::" + event.getProperty();
  }

  private ThingInfo aggregateProperties(ThingEvent value, ThingInfo aggregate) {
    return aggregate.withNewProperty(value.getProperty());
  }
}
