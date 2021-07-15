package io.github.malczuuu.uiot.things.core;

import io.github.malczuuu.uiot.schema.event.thing.ThingCreateEvent;
import io.github.malczuuu.uiot.schema.event.thing.ThingDeleteEvent;
import io.github.malczuuu.uiot.things.entity.ThingEntity;
import io.github.malczuuu.uiot.things.entity.ThingRepository;
import io.github.malczuuu.uiot.things.model.CursorPage;
import io.github.malczuuu.uiot.things.model.ThingCreateModel;
import io.github.malczuuu.uiot.things.model.ThingModel;
import io.github.malczuuu.uiot.things.model.ThingUpdateModel;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class ThingServiceImpl implements ThingService {

  private final ThingRepository thingRepository;
  private final ThingEventBroker thingEventBroker;
  private final Clock clock;

  public ThingServiceImpl(
      ThingRepository thingRepository, ThingEventBroker thingEventBroker, Clock clock) {
    this.thingRepository = thingRepository;
    this.thingEventBroker = thingEventBroker;
    this.clock = clock;
  }

  @Override
  public CursorPage<ThingModel> getThings(String roomUid, int size) {
    List<ThingEntity> entities = thingRepository.findWithoutCursor(roomUid, size);
    return toNextPage(entities, size);
  }

  private CursorPage<ThingModel> toNextPage(List<ThingEntity> entities, int size) {
    String cursor =
        entities.size() == size ? entities.get(size - 1).getId().toHexString() + "." + size : null;
    return new CursorPage<>(
        entities.stream().map(this::toThingModel).collect(Collectors.toList()),
        cursor != null ? new CursorPage.Links("/api/things?cursor=" + cursor) : null);
  }

  @Override
  public CursorPage<ThingModel> getThings(String roomUid, String cursorString)
      throws InvalidCursorException {
    Cursor cursor = Cursor.from(cursorString).orElseThrow(InvalidCursorException::new);
    List<ThingEntity> entities = thingRepository.findWithCursor(roomUid, cursor.id, cursor.size);
    return toNextPage(entities, cursor.size);
  }

  @Override
  public ThingModel getThing(String roomUid, String thingUid) throws ThingNotFoundException {
    ThingEntity entity = fetchThing(roomUid, thingUid);
    return toThingModel(entity);
  }

  private ThingEntity fetchThing(String roomUid, String thingUid) {
    return thingRepository
        .findByRoomAndUid(roomUid, thingUid)
        .orElseThrow(ThingNotFoundException::new);
  }

  @Override
  public ThingModel requestThingCreation(String roomUid, ThingCreateModel thing) {
    ThingModel model = new ThingModel(thing.getUid(), thing.getName(), null);
    long time = nowAsNano();
    thingEventBroker.publish(new ThingCreateEvent(roomUid, model.getUid(), model.getName(), time));
    return model;
  }

  @Override
  public void createThing(String roomUid, ThingModel thing) {
    ThingEntity entity = new ThingEntity(roomUid, thing.getUid(), thing.getName());
    thingRepository.save(entity);
  }

  @Override
  public ThingModel updateThing(String roomUid, String thingUid, ThingUpdateModel thing)
      throws ThingNotFoundException {
    ThingEntity entity = fetchThing(roomUid, thingUid);
    entity.setName(thing.getName());
    entity.setVersion(thing.getVersion());
    entity = thingRepository.save(entity);
    return toThingModel(entity);
  }

  private ThingModel toThingModel(ThingEntity entity) {
    return new ThingModel(entity.getUid(), entity.getName(), entity.getVersion());
  }

  @Override
  public void requestThingDeletion(String roomUid, String thingUid) {
    if (thingRepository.existsByRoomAndUid(roomUid, thingUid)) {
      long time = nowAsNano();
      thingEventBroker.publish(new ThingDeleteEvent(roomUid, thingUid, time));
    }
  }

  private long nowAsNano() {
    Instant timestamp = clock.instant();
    return timestamp.getEpochSecond() * 1000_000_000L + timestamp.getNano();
  }

  @Override
  public void deleteThing(String thingUid, String roomUid) {
    thingRepository.deleteByRoomAndUid(roomUid, roomUid);
  }

  private static final class Cursor {

    private static Optional<Cursor> from(String cursor) {
      String[] split = cursor.split("\\.", 2);
      if (split.length != 2 || !ObjectId.isValid(split[0])) {
        return Optional.empty();
      }
      try {
        int size = Integer.parseInt(split[1]);
        return Optional.of(new Cursor(new ObjectId(split[0]), size));
      } catch (NumberFormatException e) {
        return Optional.empty();
      }
    }

    private final ObjectId id;
    private final Integer size;

    private Cursor(ObjectId id, Integer size) {
      this.id = id;
      this.size = size;
    }
  }
}
