package io.github.malczuuu.uiot.rules.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.rule.ActionExecutionEnvelope;
import io.github.malczuuu.uiot.models.thing.ThingEvent;
import io.github.malczuuu.uiot.models.thing.ThingEventEnvelope;
import io.github.malczuuu.uiot.rules.core.RuleService;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
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
public class ThingEventStream implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(ThingEventStream.class);

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;

  private final RuleService ruleService;

  private final String thingEventsTopic;
  private final String actionExecutionEventsTopic;

  public ThingEventStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      RuleService ruleService,
      @Value("${uiot.thing-events-topic}") String thingEventsTopic,
      @Value("${uiot.action-execution-events-topic}") String actionExecutionEventsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.ruleService = ruleService;
    this.thingEventsTopic = thingEventsTopic;
    this.actionExecutionEventsTopic = actionExecutionEventsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    streamsBuilder.stream(
            thingEventsTopic,
            Consumed.<String, ThingEventEnvelope>as("thing_events_topic_source")
                .withKeySerde(Serdes.String())
                .withValueSerde(getJsonSerde(ThingEventEnvelope.class))
                .withTimestampExtractor(new WallclockTimestampExtractor())
                .withOffsetResetPolicy(AutoOffsetReset.LATEST))
        .filter((key, value) -> value.getThingEvents() != null)
        .flatMap((key, value) -> flatMapThingEvents(value))
        .flatMap((key, value) -> eventuallyTriggerAction(value))
        .to(
            actionExecutionEventsTopic,
            Produced.<String, ActionExecutionEnvelope>as("action_execution_events_sink")
                .withKeySerde(Serdes.String())
                .withValueSerde(getJsonSerde(ActionExecutionEnvelope.class)));
  }

  private <T> JsonSerde<T> getJsonSerde(Class<T> type) {
    return new JsonSerde<>(type, objectMapper).noTypeInfo().ignoreTypeHeaders();
  }

  private Iterable<KeyValue<String, ThingEvent>> flatMapThingEvents(ThingEventEnvelope envelope) {
    return envelope.getThingEvents().stream()
        .map(event -> new KeyValue<>(event.getRoom(), event))
        .collect(Collectors.toList());
  }

  private Iterable<KeyValue<String, ActionExecutionEnvelope>> eventuallyTriggerAction(
      ThingEvent value) {
    return Collections.emptyList();
  }
}
