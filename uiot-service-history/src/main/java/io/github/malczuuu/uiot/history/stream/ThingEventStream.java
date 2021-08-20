package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.history.core.HistoryService;
import io.github.malczuuu.uiot.models.ThingEvent;
import io.github.malczuuu.uiot.models.ThingEventsEnvelope;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class ThingEventStream implements InitializingBean {

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
    KStream<String, ThingEventsEnvelope> thingEventStream = initiateThingEventStream();
    storeThingEventsInDatabase(thingEventStream);
    storeThingMetadataInKTable(thingEventStream);
    storePropertyLastStateInKTable(thingEventStream);
  }

  private KStream<String, ThingEventsEnvelope> initiateThingEventStream() {
    return streamsBuilder.stream(
        topics.getThingEventsTopic(),
        Consumed.<String, ThingEventsEnvelope>as("thing_events_source")
            .withKeySerde(Serdes.String())
            .withValueSerde(getJsonSerde(ThingEventsEnvelope.class))
            .withTimestampExtractor(new WallclockTimestampExtractor())
            .withOffsetResetPolicy(AutoOffsetReset.LATEST));
  }

  private void storeThingEventsInDatabase(KStream<String, ThingEventsEnvelope> thingEventStream) {
    thingEventStream
        .filter((key, value) -> value.getThingEvents() != null)
        .foreach((key, value) -> processThingEvent(value));
  }

  private void storeThingMetadataInKTable(KStream<String, ThingEventsEnvelope> thingEventStream) {
    thingEventStream
        .flatMap((key, value) -> splitByThingKey(value))
        .groupByKey(Grouped.with(Serdes.String(), getJsonSerde(ThingEvent.class)))
        .aggregate(
            ThingModel::new,
            (key, value, aggregate) -> aggregateProperties(value, aggregate),
            Materialized.with(Serdes.String(), getJsonSerde(ThingModel.class)))
        .toStream()
        .to(
            topics.getThingMetadataTopic(),
            Produced.with(Serdes.String(), getJsonSerde(ThingModel.class)));

    streamsBuilder.globalTable(
        topics.getThingMetadataTopic(),
        Consumed.with(Serdes.String(), getJsonSerde(ThingModel.class)),
        Materialized.as(topics.getThingMetadataTopic()));
  }

  private void storePropertyLastStateInKTable(
      KStream<String, ThingEventsEnvelope> thingEventStream) {
    thingEventStream
        .flatMap((key, value) -> splitByPropertyKey(value))
        .to(
            topics.getKeyedThingEventsTopic(),
            Produced.with(Serdes.String(), getJsonSerde(ThingEvent.class)));

    streamsBuilder.globalTable(
        topics.getKeyedThingEventsTopic(),
        Consumed.with(Serdes.String(), getJsonSerde(ThingEvent.class)),
        Materialized.as(topics.getKeyedThingEventsTopic()));
  }

  private <T> JsonSerde<T> getJsonSerde(Class<T> clazz) {
    return new JsonSerde<>(clazz, objectMapper).ignoreTypeHeaders().noTypeInfo();
  }

  private void processThingEvent(ThingEventsEnvelope value) {
    value.getThingEvents().forEach(historyService::storeEvent);
  }

  private Iterable<KeyValue<String, ThingEvent>> splitByThingKey(ThingEventsEnvelope value) {
    if (value.getThingEvents() != null) {
      return value.getThingEvents().stream()
          .map(event -> new KeyValue<>(getThingKey(event), event))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private Iterable<KeyValue<String, ThingEvent>> splitByPropertyKey(ThingEventsEnvelope value) {
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

  private ThingModel aggregateProperties(ThingEvent value, ThingModel aggregate) {
    return aggregate.withNewProperty(value.getProperty());
  }
}
