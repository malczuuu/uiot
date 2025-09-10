package io.github.malczuuu.uiot.rooms.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.Envelope;
import io.github.malczuuu.uiot.models.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
import io.github.malczuuu.uiot.rooms.core.RoomService;
import java.util.Map;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
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
  private final RoomService roomService;

  private final String systemEventsTopic;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      RoomService roomService,
      @Value("${uiot.system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.roomService = roomService;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    Map<String, KStream<String, Envelope>> branches =
        streamsBuilder.stream(
                systemEventsTopic,
                Consumed.<String, Envelope>as("system_events_source")
                    .withKeySerde(Serdes.String())
                    .withValueSerde(getEnvelopeSerde())
                    .withTimestampExtractor(new WallclockTimestampExtractor())
                    .withOffsetResetPolicy(AutoOffsetReset.LATEST))
            .split(Named.as("system_events_"))
            .branch((key, value) -> value instanceof RoomCreateEnvelope, Branched.as("room_create"))
            .branch((key, value) -> value instanceof RoomDeleteEnvelope, Branched.as("room_delete"))
            .noDefaultBranch();

    branches
        .get("system_events_room_create")
        .mapValues(value -> (RoomCreateEnvelope) value)
        .mapValues(RoomCreateEnvelope::getRoomCreateEvent)
        .foreach((key, value) -> triggerRoomCreation(value));

    branches
        .get("system_events_room_delete")
        .mapValues(value -> (RoomDeleteEnvelope) value)
        .mapValues(RoomDeleteEnvelope::getRoomDeleteEvent)
        .foreach((key, value) -> triggerRoomDeletion(value));
  }

  private JsonSerde<Envelope> getEnvelopeSerde() {
    return new JsonSerde<>(Envelope.class, objectMapper).noTypeInfo().ignoreTypeHeaders();
  }

  private void triggerRoomCreation(RoomCreateEvent event) {
    roomService.createRoom(event);

    log.info("Triggered room creation via streams processing, room={}", event.getRoomUid());
  }

  private void triggerRoomDeletion(RoomDeleteEvent event) {
    roomService.deleteRoom(event.getRoomUid());

    log.info("Triggered room deletion via streams processing, room_uid={}", event.getRoomUid());
  }
}
