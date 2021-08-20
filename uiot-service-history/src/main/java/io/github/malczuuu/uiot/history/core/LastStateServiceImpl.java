package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.history.model.EventHistory;
import io.github.malczuuu.uiot.history.model.HistoryRecord;
import io.github.malczuuu.uiot.history.stream.ThingModel;
import io.github.malczuuu.uiot.history.stream.TopicProperties;
import io.github.malczuuu.uiot.models.ThingEvent;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class LastStateServiceImpl implements LastStateService {

  private final StreamsBuilderFactoryBean kafkaStreams;

  private final TopicProperties topics;

  public LastStateServiceImpl(StreamsBuilderFactoryBean kafkaStreams, TopicProperties topics) {
    this.kafkaStreams = kafkaStreams;
    this.topics = topics;
  }

  @Override
  public EventHistory getLastState(String roomUid, String thingUid, String propertyName) {
    ThingEvent event = getLastStateStore().get(getRoomKey(roomUid, thingUid, propertyName));
    if (event == null) {
      return new EventHistory(new ArrayList<>());
    }
    OffsetDateTime parsedTime = parseTime(event.getTime());
    OffsetDateTime parsedArrivalTime = parseTime(event.getArrivalTime());

    return new EventHistory(
        Collections.singletonList(
            new HistoryRecord(
                event.getName(),
                event.getTime() / 1000_000_000.0d,
                parsedTime.toString(),
                event.getValue(),
                event.getValueString(),
                event.getValueBoolean(),
                event.getValueData(),
                event.getUnit(),
                event.getRoom(),
                event.getThing(),
                event.getProperty(),
                event.getArrivalTime() / 1000_000_000.0d,
                parsedArrivalTime.toString())));
  }

  private String getRoomKey(String roomUid, String thingUid, String propertyName) {
    return roomUid + "::" + thingUid + "::" + propertyName;
  }

  private OffsetDateTime parseTime(long time) {
    return Instant.ofEpochSecond(time / 1000_000_000L, time % 1000_000_000L)
        .atOffset(ZoneOffset.UTC);
  }

  private ReadOnlyKeyValueStore<String, ThingEvent> getLastStateStore() {
    return kafkaStreams
        .getKafkaStreams()
        .store(
            StoreQueryParameters.fromNameAndType(
                topics.getKeyedThingEventsTopic(), QueryableStoreTypes.keyValueStore()));
  }

  private ReadOnlyKeyValueStore<String, ThingModel> getThingInfoStore() {
    return kafkaStreams
        .getKafkaStreams()
        .store(
            StoreQueryParameters.fromNameAndType(
                topics.getThingMetadataTopic(), QueryableStoreTypes.keyValueStore()));
  }

  @Override
  public EventHistory getLastState(String roomUid, String thingUid) {
    ThingModel model = getThingInfoStore().get(getThingKey(roomUid, thingUid));
    if (model == null) {
      return new EventHistory(new ArrayList<>());
    }

    return new EventHistory(
        model.getProperties().stream()
            .sorted()
            .flatMap(property -> getLastState(roomUid, thingUid, property).stream())
            .collect(Collectors.toList()));
  }

  private String getThingKey(String roomUid, String thingUid) {
    return roomUid + "::" + thingUid;
  }
}
