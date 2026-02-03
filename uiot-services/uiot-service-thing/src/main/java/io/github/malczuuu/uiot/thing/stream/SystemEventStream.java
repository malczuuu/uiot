package io.github.malczuuu.uiot.thing.stream;

import io.github.malczuuu.uiot.model.dto.Envelope;
import io.github.malczuuu.uiot.model.dto.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.model.dto.RoomDeleteEvent;
import io.github.malczuuu.uiot.thing.core.ThingService;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.AutoOffsetReset;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableKafkaStreams
public class SystemEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(SystemEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final JsonMapper jsonMapper;

  private final ThingService thingService;

  private final String systemEventsTopic;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      JsonMapper jsonMapper,
      ThingService thingService,
      @Value("${uiot.system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.jsonMapper = jsonMapper;
    this.thingService = thingService;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    streamsBuilder.stream(
            systemEventsTopic,
            Consumed.<String, Envelope>as("system_events_source")
                .withKeySerde(Serdes.String())
                .withValueSerde(getEnvelopeSerde())
                .withTimestampExtractor(new WallclockTimestampExtractor())
                .withOffsetResetPolicy(AutoOffsetReset.latest()))
        .filter((_, value) -> value instanceof RoomDeleteEnvelope)
        .mapValues(value -> (RoomDeleteEnvelope) value)
        .mapValues(RoomDeleteEnvelope::getRoomDeleteEvent)
        .foreach((_, value) -> deleteThings(value));
  }

  @SuppressWarnings("resource")
  private JacksonJsonSerde<Envelope> getEnvelopeSerde() {
    return new JacksonJsonSerde<>(Envelope.class, jsonMapper).noTypeInfo().ignoreTypeHeaders();
  }

  private void deleteThings(RoomDeleteEvent event) {
    log.debug("Requested resources deletion for room_uid={}", event.getRoomUid());
    thingService.deleteThings(event.getRoomUid());
    log.info("Deleted resources (things) for room_uid={}", event.getRoomUid());
  }
}
