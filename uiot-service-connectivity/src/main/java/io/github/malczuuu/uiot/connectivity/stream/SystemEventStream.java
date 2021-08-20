package io.github.malczuuu.uiot.connectivity.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.connectivity.core.ConnectivityService;
import io.github.malczuuu.uiot.models.Envelope;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
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
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class SystemEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(SystemEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;

  private final ConnectivityService thingService;

  private final String systemEventsTopic;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      ConnectivityService thingService,
      @Value("${uiot.system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
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
                .withOffsetResetPolicy(AutoOffsetReset.LATEST))
        .filter((key, value) -> value instanceof RoomDeleteEnvelope)
        .mapValues(value -> (RoomDeleteEnvelope) value)
        .mapValues(RoomDeleteEnvelope::getRoomDeleteEvent)
        .foreach((key, value) -> deleteConnectivity(value));
  }

  private JsonSerde<Envelope> getEnvelopeSerde() {
    return new JsonSerde<>(Envelope.class, objectMapper).noTypeInfo().ignoreTypeHeaders();
  }

  private void deleteConnectivity(RoomDeleteEvent event) {
    log.debug("Requested resources deletion for room_uid={}", event.getRoomUid());
    thingService.deleteConnectivity(event.getRoomUid());
    log.info("Deleted resources (connectivity) for room_uid={}", event.getRoomUid());
  }
}
