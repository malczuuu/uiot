package io.github.malczuuu.uiot.rabbitmq.telemetry;

import io.github.malczuuu.uiot.model.dto.AccountingMetric;
import io.github.malczuuu.uiot.model.dto.AccountingMetricEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
public class AccountingKafkaSink {

  private static final Logger log = LoggerFactory.getLogger(AccountingKafkaSink.class);

  private final KafkaOperations<String, String> kafkaOperations;
  private final JsonMapper jsonMapper;
  private final String kafkaOutputTopic;

  public AccountingKafkaSink(
      KafkaOperations<String, String> kafkaOperations,
      JsonMapper jsonMapper,
      @Value("${uiot.kafka-accounting-topic}") String kafkaOutputTopic) {
    this.kafkaOperations = kafkaOperations;
    this.jsonMapper = jsonMapper;
    this.kafkaOutputTopic = kafkaOutputTopic;
  }

  public void sink(AccountingMetric accountingMetric) {
    AccountingMetricEnvelope envelope = new AccountingMetricEnvelope(accountingMetric);
    String payload;
    try {
      payload = jsonMapper.writeValueAsString(envelope);
    } catch (JacksonException e) {
      log.error("Unable to write envelope as a JSON message, envelope={}", envelope, e);
      return;
    }
    kafkaOperations.send(kafkaOutputTopic, payload);
  }
}
