package io.github.malczuuu.uiot.accounting.stream;

import io.github.malczuuu.uiot.accounting.core.AccountingEntity;
import io.github.malczuuu.uiot.accounting.core.AccountingRepository;
import io.github.malczuuu.uiot.model.dto.AccountingWindow;
import io.github.malczuuu.uiot.model.dto.AccountingWindowEnvelope;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableKafkaStreams
public class AggregateStream implements InitializingBean {

  private final StreamsBuilder streamsBuilder;
  private final JsonMapper jsonMapper;
  private final AccountingRepository accountingRepository;

  private final String windowsTopic;

  public AggregateStream(
      StreamsBuilder streamsBuilder,
      JsonMapper jsonMapper,
      AccountingRepository accountingRepository,
      @Value("${uiot.windows-topic}") String windowsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.jsonMapper = jsonMapper;
    this.accountingRepository = accountingRepository;
    this.windowsTopic = windowsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    streamsBuilder.stream(windowsTopic, Consumed.with(Serdes.String(), newBasicJsonSerde()))
        .filter((_, value) -> value.getWindowEvent() != null)
        .mapValues(AccountingWindowEnvelope::getWindowEvent)
        .foreach((_, value) -> storeAccountingAggregate(value));
  }

  @SuppressWarnings("resource")
  private JacksonJsonSerde<AccountingWindowEnvelope> newBasicJsonSerde() {
    return new JacksonJsonSerde<>(AccountingWindowEnvelope.class, jsonMapper)
        .ignoreTypeHeaders()
        .noTypeInfo();
  }

  private void storeAccountingAggregate(AccountingWindow value) {
    AccountingEntity entity = toEntity(value);
    accountingRepository.upsert(entity);
  }

  private AccountingEntity toEntity(AccountingWindow value) {
    return new AccountingEntity(
        value.getUuid(),
        value.getRoomUid(),
        value.getType(),
        value.getTags(),
        value.getTimes().get(0),
        value.getTimes().get(1),
        value.getValue());
  }
}
