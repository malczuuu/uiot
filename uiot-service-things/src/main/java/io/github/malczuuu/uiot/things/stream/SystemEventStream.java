package io.github.malczuuu.uiot.things.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.Envelope;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
import io.github.malczuuu.uiot.things.core.ThingService;
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

  private final ThingService thingService;

  private final String systemEventsTopic;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      ThingService thingService,
      @Value("${uiot.system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.thingService = thingService;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    setupSystemEventStream();
  }

  private void setupSystemEventStream() {
    Consumed<String, Envelope> consumed =
        Consumed.with(
            Serdes.String(),
            new JsonSerde<>(Envelope.class, objectMapper).noTypeInfo().ignoreTypeHeaders(),
            new WallclockTimestampExtractor(),
            AutoOffsetReset.LATEST);
    streamsBuilder.stream(systemEventsTopic, consumed)
        .filter((key, value) -> value instanceof RoomDeleteEnvelope)
        .mapValues(value -> (RoomDeleteEnvelope) value)
        .foreach((key, value) -> deleteThings(value));
  }

  private void deleteThings(RoomDeleteEnvelope envelope) {
    RoomDeleteEvent event = envelope.getRoomDeleteEvent();
    thingService.deleteThings(event.getRoomUid());
  }
}
