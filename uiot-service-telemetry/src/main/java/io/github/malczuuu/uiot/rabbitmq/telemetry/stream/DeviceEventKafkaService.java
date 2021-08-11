package io.github.malczuuu.uiot.rabbitmq.telemetry.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.thing.ThingEvent;
import io.github.malczuuu.uiot.models.thing.ThingEventEnvelope;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

@Service
public class DeviceEventKafkaService {

  private static final Logger log = LoggerFactory.getLogger(DeviceEventKafkaService.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final ObjectMapper objectMapper;
  private final String kafkaOutputTopic;

  public DeviceEventKafkaService(
      KafkaOperations<String, String> kafkaOperations,
      ObjectMapper objectMapper,
      @Value("${uiot.telemetry.kafka-output-topic}") String kafkaOutputTopic) {
    this.kafkaOperations = kafkaOperations;
    this.objectMapper = objectMapper;
    this.kafkaOutputTopic = kafkaOutputTopic;
  }

  public void sink(List<ThingEvent> thingEvents) {
    ThingEventEnvelope envelope = new ThingEventEnvelope(thingEvents);
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
