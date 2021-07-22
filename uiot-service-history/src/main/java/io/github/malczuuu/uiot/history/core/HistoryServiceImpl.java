package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.history.model.EventHistory;
import io.github.malczuuu.uiot.history.model.HistoryRecord;
import io.github.malczuuu.uiot.schema.event.thing.ThingEvent;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

  private final ThingEventRepositoryExtensions thingEventRepository;

  public HistoryServiceImpl(ThingEventRepositoryExtensions thingEventRepository) {
    this.thingEventRepository = thingEventRepository;
  }

  @Override
  public EventHistory getEventHistory(String roomUid, int size) {
    List<ThingEventEntity> entities = thingEventRepository.findFirstPage(roomUid, size);
    return new EventHistory(
        entities.stream().map(this::toHistoryRecord).collect(Collectors.toList()));
  }

  @Override
  public EventHistory getThingEventHistory(String roomUid, String thingUid, int size) {
    List<ThingEventEntity> entities = thingEventRepository.findFirstPage(roomUid, thingUid, size);
    return new EventHistory(
        entities.stream().map(this::toHistoryRecord).collect(Collectors.toList()));
  }

  private HistoryRecord toHistoryRecord(ThingEventEntity e) {
    OffsetDateTime parsedTime = parseTime(e.getTime());
    OffsetDateTime parsedArrivalTime = parseTime(e.getArrivalTime());

    return new HistoryRecord(
        e.getName(),
        e.getTime() / 1000_000_000.0d,
        parsedTime.toString(),
        e.getValue(),
        e.getValueString(),
        e.getValueBoolean(),
        e.getValueData(),
        e.getUnit(),
        e.getRoomUid(),
        e.getThingUid(),
        e.getPropertyName(),
        e.getArrivalTime() / 1000_000_000.0d,
        parsedArrivalTime.toString());
  }

  private OffsetDateTime parseTime(long time) {
    return Instant.ofEpochSecond(time / 1000_000_000L, time % 1000_000_000L)
        .atOffset(ZoneOffset.UTC);
  }

  @Override
  public void storeEvent(ThingEvent event) {
    ThingEventEntity entity = toEntity(event);
    thingEventRepository.save(entity);
  }

  private ThingEventEntity toEntity(ThingEvent event) {
    return new ThingEventEntity(
        event.getName(),
        event.getTime(),
        event.getValue(),
        event.getValueString(),
        event.getValueBoolean(),
        event.getValueData(),
        event.getUnit(),
        event.getId(),
        event.getRoom(),
        event.getThing(),
        event.getProperty(),
        event.getArrivalTime());
  }
}
