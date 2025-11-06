package io.github.malczuuu.uiot.rabbitmq.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.AccountingMetric;
import io.github.malczuuu.uiot.models.AccountingMetricEnvelope;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

@Service
public class AccountingKafkaSink {

  private static final Logger log = LoggerFactory.getLogger(AccountingKafkaSink.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final ObjectMapper objectMapper;
  private final String kafkaOutputTopic;

  public AccountingKafkaSink(
      KafkaOperations<String, String> kafkaOperations,
      ObjectMapper objectMapper,
      @Value("${uiot.kafka-accounting-topic}") String kafkaOutputTopic) {
    this.kafkaOperations = kafkaOperations;
    this.objectMapper = objectMapper;
    this.kafkaOutputTopic = kafkaOutputTopic;
  }

  public void sink(AccountingMetric accountingMetric) {
    AccountingMetricEnvelope envelope = new AccountingMetricEnvelope(accountingMetric);
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
