package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.history.core.StorageService;
import io.github.malczuuu.uiot.models.Envelope;
import io.github.malczuuu.uiot.models.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

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
    KStream<String, Envelope>[] subStreams =
        streamsBuilder.stream(
                topics.getSystemEventsTopic(),
                Consumed.<String, Envelope>as("system_events_source")
                    .withKeySerde(Serdes.String())
                    .withValueSerde(getEnvelopeSerde())
                    .withTimestampExtractor(new WallclockTimestampExtractor())
                    .withOffsetResetPolicy(AutoOffsetReset.LATEST))
            .branch(
                (key, value) -> value instanceof RoomCreateEnvelope,
                (key, value) -> value instanceof RoomDeleteEnvelope);
    subStreams[0]
        .mapValues(value -> (RoomCreateEnvelope) value)
        .mapValues(RoomCreateEnvelope::getRoomCreateEvent)
        .foreach((key, value) -> createStorage(value));
    subStreams[1]
        .mapValues(value -> (RoomDeleteEnvelope) value)
        .mapValues(RoomDeleteEnvelope::getRoomDeleteEvent)
        .foreach((key, value) -> deleteStorage(value));
  }

  private JsonSerde<Envelope> getEnvelopeSerde() {
    return new JsonSerde<>(Envelope.class, objectMapper).noTypeInfo().ignoreTypeHeaders();
  }

  private void createStorage(RoomCreateEvent event) {
    log.debug("Requested resources creation for room_uid={}", event.getRoomUid());
    storageService.createStorage(event);
    log.info("Created resources (storage) for room_uid={}", event.getRoomUid());
  }

  private void deleteStorage(RoomDeleteEvent event) {
    log.debug("Requested resources deletion for room_uid={}", event.getRoomUid());
    storageService.deleteStorage(event);
    log.info("Deleted resources (storage) for room_uid={}", event.getRoomUid());
  }
}
