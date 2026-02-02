package io.github.malczuuu.uiot.accounting.stream;

import io.github.malczuuu.uiot.models.AccountingMetric;
import io.github.malczuuu.uiot.models.AccountingMetricEnvelope;
import io.github.malczuuu.uiot.models.AccountingWindow;
import io.github.malczuuu.uiot.models.AccountingWindowEnvelope;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
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
import org.springframework.kafka.support.serializer.JacksonJsonSerde;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableKafkaStreams
public class AccountingStream implements InitializingBean {

  private final StreamsBuilder streamsBuilder;
  private final JsonMapper jsonMapper;

  private final String metricsTopic;
  private final String windowsTopic;

  private final Duration windowsSize;
  private final Duration gracePeriod = Duration.ofSeconds(5);

  public AccountingStream(
      StreamsBuilder streamsBuilder,
      JsonMapper jsonMapper,
      @Value("${uiot.metrics-topic}") String metricsTopic,
      @Value("${uiot.windows-topic}") String windowsTopic,
      @Value("${uiot.windows-size}") Duration windowsSize) {
    this.streamsBuilder = streamsBuilder;
    this.jsonMapper = jsonMapper;
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
                .withValueSerde(getJsonSerde(AccountingMetricEnvelope.class)))
        .filter((_, value) -> value.getAccountingEvent() != null)
        .mapValues(AccountingMetricEnvelope::getAccountingEvent)
        .groupBy(
            (_, value) -> new WindowKey(value.getType(), value.getRoomUid(), value.getTags()),
            Grouped.<WindowKey, AccountingMetric>as("metric_grouping")
                .withKeySerde(getJsonSerde(WindowKey.class))
                .withValueSerde(getJsonSerde(AccountingMetric.class)))
        .windowedBy(TimeWindows.ofSizeAndGrace(windowsSize, gracePeriod))
        .aggregate(
            () -> new Aggregate(UUID.randomUUID().toString(), 0.0),
            (_, value, aggregate) -> aggregate.aggregate(value.getValue()),
            Named.as("accounting_windowing"),
            Materialized.<WindowKey, Aggregate>as(
                    Stores.inMemoryWindowStore(
                        "window_store",
                        windowsSize.plus(gracePeriod).multipliedBy(2),
                        windowsSize,
                        true))
                .withKeySerde(getJsonSerde(WindowKey.class))
                .withValueSerde(getJsonSerde(Aggregate.class)))
        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
        .toStream()
        .map(this::mapAccountingModel, Named.as("accounting_windowing_stream"))
        .to(
            windowsTopic,
            Produced.<String, AccountingWindowEnvelope>as("accounting_source")
                .withKeySerde(Serdes.String())
                .withValueSerde(getJsonSerde(AccountingWindowEnvelope.class)));
  }

  @SuppressWarnings("resource")
  private <T> JacksonJsonSerde<T> getJsonSerde(Class<T> clazz) {
    return new JacksonJsonSerde<>(clazz, jsonMapper).ignoreTypeHeaders().noTypeInfo();
  }

  private KeyValue<String, AccountingWindowEnvelope> mapAccountingModel(
      Windowed<WindowKey> key, Aggregate value) {
    return new KeyValue<>(
        key.key().getRoomUid(),
        new AccountingWindowEnvelope(
            new AccountingWindow(
                value.getUuid(),
                key.key().getType(),
                key.key().getRoomUid(),
                value.getValue(),
                List.of(toMillis(key.window().startTime()), toMillis(key.window().endTime())),
                key.key().getTags())));
  }

  private long toMillis(Instant instant) {
    return instant.getEpochSecond() * 1000_000_000L + instant.getNano();
  }
}
