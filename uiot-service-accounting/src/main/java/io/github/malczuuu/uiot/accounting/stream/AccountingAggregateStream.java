package io.github.malczuuu.uiot.accounting.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.accounting.core.AccountingEntity;
import io.github.malczuuu.uiot.accounting.core.AccountingRepository;
import io.github.malczuuu.uiot.models.accounting.AccountingAggregate;
import io.github.malczuuu.uiot.models.accounting.AccountingAggregateEnvelope;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class AccountingAggregateStream implements InitializingBean {

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;
  private final AccountingRepository accountingRepository;

  private final String windowsTopic;

  public AccountingAggregateStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      AccountingRepository accountingRepository,
      @Value("${uiot.accounting.windows-topic}") String windowsTopic) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.accountingRepository = accountingRepository;
    this.windowsTopic = windowsTopic;
  }

  @Override
  public void afterPropertiesSet() {
    streamsBuilder.stream(windowsTopic, Consumed.with(Serdes.String(), newBasicJsonSerde()))
        .filter((key, value) -> value.getWindowEvent() != null)
        .mapValues(AccountingAggregateEnvelope::getWindowEvent)
        .foreach((key, value) -> storeAccountingAggregate(value));
  }

  private JsonSerde<AccountingAggregateEnvelope> newBasicJsonSerde() {
    return new JsonSerde<>(AccountingAggregateEnvelope.class, objectMapper)
        .ignoreTypeHeaders()
        .noTypeInfo();
  }

  private void storeAccountingAggregate(AccountingAggregate value) {
    AccountingEntity entity = toEntity(value);
    accountingRepository.upsert(entity);
  }

  private AccountingEntity toEntity(AccountingAggregate value) {
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