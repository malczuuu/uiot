package io.github.malczuuu.uiot.accounting.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.uiot.models.accounting.AccountingModel;
import io.github.malczuuu.uiot.models.accounting.MetricModel;
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
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.internals.InMemoryWindowBytesStoreSupplier;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class MetricStream implements InitializingBean {

  private final StreamsBuilder streamsBuilder;
  private final ObjectMapper objectMapper;

  private final String metricsTopic;
  private final String windowsTopic;

  private final Duration windowsSize;

  public MetricStream(
      StreamsBuilder streamsBuilder,
      ObjectMapper objectMapper,
      @Value("${uiot.accounting.metrics-topic}") String metricsTopic,
      @Value("${uiot.accounting.windows-topic}") String windowsTopic,
      @Value("${uiot.accounting.windows-size}") Duration windowsSize) {
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
            Consumed.<String, MetricModel>as("metrics_source")
                .withTimestampExtractor(new WallclockTimestampExtractor())
                .withKeySerde(Serdes.String())
                .withValueSerde(newBasicJsonSerde(MetricModel.class)))
        .groupBy(
            (key, value) -> new MetricKey(value.getType(), value.getRoomUid(), value.getTags()),
            Grouped.<MetricKey, MetricModel>as("metric_grouping")
                .withKeySerde(newBasicJsonSerde(MetricKey.class))
                .withValueSerde(newBasicJsonSerde(MetricModel.class)))
        .windowedBy(TimeWindows.of(windowsSize))
        .aggregate(
            () -> new MetricAggregate(UUID.randomUUID().toString(), 0.0),
            (key, value, aggregate) -> aggregate.aggregate(value.getValue()),
            Named.as("accounting_windowing"),
            Materialized.<MetricKey, MetricAggregate>as(
                    new InMemoryWindowBytesStoreSupplier(
                        "window_store",
                        Duration.ofSeconds(5).toMillis(),
                        windowsSize.toMillis(),
                        false))
                .withKeySerde(newBasicJsonSerde(MetricKey.class))
                .withValueSerde(newBasicJsonSerde(MetricAggregate.class)))
        .toStream()
        .map(this::mapAccountingModel, Named.as("accounting_windowing_stream"))
        .to(
            windowsTopic,
            Produced.<String, AccountingModel>as("accounting_source")
                .withKeySerde(Serdes.String())
                .withValueSerde(newBasicJsonSerde(AccountingModel.class)));
  }

  private <T> JsonSerde<T> newBasicJsonSerde(Class<T> clazz) {
    return new JsonSerde<>(clazz, objectMapper).ignoreTypeHeaders().noTypeInfo();
  }

  private KeyValue<String, AccountingModel> mapAccountingModel(
      Windowed<MetricKey> key, MetricAggregate value) {
    return new KeyValue<>(
        key.key().getRoomUid(),
        new AccountingModel(
            value.getUuid(),
            key.key().getType(),
            key.key().getRoomUid(),
            value.getValue(),
            Arrays.asList(toSeconds(key.window().startTime()), toSeconds(key.window().endTime())),
            key.key().getTags()));
  }

  private double toSeconds(Instant instant) {
    return instant.getEpochSecond() + 0.000_000_001 * instant.getNano();
  }
}
