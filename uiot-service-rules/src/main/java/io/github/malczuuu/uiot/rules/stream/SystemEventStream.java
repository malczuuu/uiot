package io.github.malczuuu.uiot.rules.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;
import io.github.malczuuu.uiot.rules.core.RuleService;
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

  private final RuleService ruleService;

  private final String systemEventsTopic;

  public SystemEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      RuleService ruleService,
      @Value("${uiot.system-events-topic}") String systemEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.ruleService = ruleService;
    this.systemEventsTopic = systemEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    setupSystemEventStream();
  }

  private void setupSystemEventStream() {
    streamsBuilder.stream(
            systemEventsTopic,
            Consumed.<String, JsonNode>as("system_events_source")
                .withKeySerde(Serdes.String())
                .withValueSerde(getJsonNodeSerde())
                .withTimestampExtractor(new WallclockTimestampExtractor())
                .withOffsetResetPolicy(AutoOffsetReset.LATEST))
        .foreach((key, value) -> processSystemEvent(value));
  }

  private JsonSerde<JsonNode> getJsonNodeSerde() {
    return new JsonSerde<>(JsonNode.class, objectMapper).noTypeInfo().ignoreTypeHeaders();
  }

  private void processSystemEvent(JsonNode node) {
    try {
      if (node.hasNonNull(RoomDeleteEnvelope.TYPE)) {
        RoomDeleteEnvelope envelope = objectMapper.treeToValue(node, RoomDeleteEnvelope.class);
        RoomDeleteEvent event = envelope.getRoomDeleteEvent();
        ruleService.deleteRules(event.getRoomUid());
      }
    } catch (JsonProcessingException ignored) {
    }
  }
}
