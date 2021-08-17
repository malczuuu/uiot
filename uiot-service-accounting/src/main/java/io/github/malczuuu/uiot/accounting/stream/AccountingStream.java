package io.github.malczuuu.uiot.accounting.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.accounting.AccountingAggregateEnvelope;
import io.github.malczuuu.uiot.models.accounting.AccountingMetric;
import io.github.malczuuu.uiot.models.accounting.AccountingMetricEnvelope;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class AccountingStream implements InitializingBean {

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;

  private final String metricsTopic;
  private final String windowsTopic;

  private final Duration windowsSize;
  private final Duration gracePeriod = Duration.ofSeconds(5);

  public AccountingStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      @Value("${uiot.metrics-topic}") String metricsTopic,
      @Value("${uiot.windows-topic}") String windowsTopic,
      @Value("${uiot.windows-size}") Duration windowsSize) {
    this.streamsBuilder = streamsBuilder;
    this.objectMapper = objectMapper;
    this.metricsTopic = metricsTopic;
    this.windowsTopic = windowsTopic;
    this.windowsSize = windowsSize;
  }

  @Override
  public void afterPropertiesSet() {
    streamsBuilder.stream(
            metricsTopic,
            Consumed.<String, AccountingMetricEnvelope>as("metrics_source")
                .withTimestampExtractor(new WallclockTimestampExtractor())
                .withKeySerde(Serdes.String())
                .withValueSerde(newBasicJsonSerde(AccountingMetricEnvelope.class)))
        .filter((key, value) -> value.getAccountingEvent() != null)
        .mapValues(AccountingMetricEnvelope::getAccountingEvent)
        .groupBy(
            (key, value) ->
                new AggregationKey(value.getType(), value.getRoomUid(), value.getTags()),
            Grouped.<AggregationKey, AccountingMetric>as("metric_grouping")
                .withKeySerde(newBasicJsonSerde(AggregationKey.class))
                .withValueSerde(newBasicJsonSerde(AccountingMetric.class)))
        .windowedBy(TimeWindows.of(windowsSize).grace(gracePeriod))
        .aggregate(
            () -> new AccountingAggregate(UUID.randomUUID().toString(), 0.0),
            (key, value, aggregate) -> aggregate.aggregate(value.getValue()),
            Named.as("accounting_windowing"),
            Materialized.<AggregationKey, AccountingAggregate>as(
                    Stores.inMemoryWindowStore(
                        "window_store",
                        windowsSize.plus(gracePeriod).multipliedBy(2),
                        windowsSize,
                        true))
                .withKeySerde(newBasicJsonSerde(AggregationKey.class))
                .withValueSerde(newBasicJsonSerde(AccountingAggregate.class)))
        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
        .toStream()
        .map(this::mapAccountingModel, Named.as("accounting_windowing_stream"))
        .to(
            windowsTopic,
            Produced.<String, AccountingAggregateEnvelope>as("accounting_source")
                .withKeySerde(Serdes.String())
                .withValueSerde(newBasicJsonSerde(AccountingAggregateEnvelope.class)));
  }

  private <T> JsonSerde<T> newBasicJsonSerde(Class<T> clazz) {
    return new JsonSerde<>(clazz, objectMapper).ignoreTypeHeaders().noTypeInfo();
  }

  private KeyValue<String, AccountingAggregateEnvelope> mapAccountingModel(
      Windowed<AggregationKey> key, AccountingAggregate value) {
    return new KeyValue<>(
        key.key().getRoomUid(),
        new AccountingAggregateEnvelope(
            new io.github.malczuuu.uiot.models.accounting.AccountingAggregate(
                value.getUuid(),
                key.key().getType(),
                key.key().getRoomUid(),
                value.getValue(),
                Arrays.asList(toMillis(key.window().startTime()), toMillis(key.window().endTime())),
                key.key().getTags())));
  }

  private long toMillis(Instant instant) {
    return instant.getEpochSecond() * 1000_000_000L + instant.getNano();
  }
}
