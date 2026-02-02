package io.github.malczuuu.uiot.history.stream;

import io.github.malczuuu.uiot.history.core.HistoryService;
import io.github.malczuuu.uiot.models.ThingEvent;
import io.github.malczuuu.uiot.models.ThingEventsEnvelope;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.AutoOffsetReset;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableKafkaStreams
public class ThingEventStream implements InitializingBean {

  private final StreamsBuilder streamsBuilder;
  private final HistoryService historyService;
  private final JsonMapper jsonMapper;

  private final TopicProperties topics;

  public ThingEventStream(
      StreamsBuilder streamsBuilder,
      HistoryService historyService,
      JsonMapper jsonMapper,
      TopicProperties topics) {
    this.streamsBuilder = streamsBuilder;
    this.historyService = historyService;
    this.jsonMapper = jsonMapper;
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
            .withOffsetResetPolicy(AutoOffsetReset.latest()));
  }

  private void storeThingEventsInDatabase(KStream<String, ThingEventsEnvelope> thingEventStream) {
    thingEventStream
        .filter((_, value) -> value.getThingEvents() != null)
        .foreach((_, value) -> processThingEvent(value));
  }

  private void storeThingMetadataInKTable(KStream<String, ThingEventsEnvelope> thingEventStream) {
    thingEventStream
        .flatMap((_, value) -> splitByThingKey(value))
        .groupByKey(Grouped.with(Serdes.String(), getJsonSerde(ThingEvent.class)))
        .aggregate(
            ThingModel::new,
            (_, value, aggregate) -> aggregateProperties(value, aggregate),
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
        .flatMap((_, value) -> splitByPropertyKey(value))
        .to(
            topics.getKeyedThingEventsTopic(),
            Produced.with(Serdes.String(), getJsonSerde(ThingEvent.class)));

    streamsBuilder.globalTable(
        topics.getKeyedThingEventsTopic(),
        Consumed.with(Serdes.String(), getJsonSerde(ThingEvent.class)),
        Materialized.as(topics.getKeyedThingEventsTopic()));
  }

  @SuppressWarnings("resource")
  private <T> JacksonJsonSerde<T> getJsonSerde(Class<T> clazz) {
    return new JacksonJsonSerde<>(clazz, jsonMapper).ignoreTypeHeaders().noTypeInfo();
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
    return List.of();
  }

  private Iterable<KeyValue<String, ThingEvent>> splitByPropertyKey(ThingEventsEnvelope value) {
    if (value.getThingEvents() != null) {
      return value.getThingEvents().stream()
          .map(event -> new KeyValue<>(getPropertyKey(event), event))
          .collect(Collectors.toList());
    }
    return List.of();
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
