package io.github.malczuuu.uiot.rabbitmq.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.ThingEvent;
import io.github.malczuuu.uiot.models.ThingEventsEnvelope;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

@Service
public class DeviceEventKafkaSink {

  private static final Logger log = LoggerFactory.getLogger(DeviceEventKafkaSink.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final ObjectMapper objectMapper;
  private final String kafkaOutputTopic;

  public DeviceEventKafkaSink(
      KafkaOperations<String, String> kafkaOperations,
      ObjectMapper objectMapper,
      @Value("${uiot.kafka-thing-events-topic}") String kafkaOutputTopic) {
    this.kafkaOperations = kafkaOperations;
    this.objectMapper = objectMapper;
    this.kafkaOutputTopic = kafkaOutputTopic;
  }

  public void sink(List<ThingEvent> thingEvents) {
    ThingEventsEnvelope envelope = new ThingEventsEnvelope(thingEvents);
    String payload;
    try {
      payload = objectMapper.writeValueAsString(envelope);
    } catch (IOException e) {
      log.error("Unable to write envelope as a JSON message, envelope={}", envelope, e);
      return;
    }
    kafkaOperations.send(kafkaOutputTopic, payload);
  }
}
