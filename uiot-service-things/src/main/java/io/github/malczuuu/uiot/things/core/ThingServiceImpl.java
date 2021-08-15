package io.github.malczuuu.uiot.things.core;

import io.github.malczuuu.uiot.problems.InternalServerErrorException;
import io.github.malczuuu.uiot.problems.InvalidCursorException;
import io.github.malczuuu.uiot.things.model.CursorPage;
import io.github.malczuuu.uiot.things.model.ThingCreateModel;
import io.github.malczuuu.uiot.things.model.ThingModel;
import io.github.malczuuu.uiot.things.model.ThingUpdateModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThingServiceImpl implements ThingService {

  private final ThingRepository thingRepository;
  private final ConnectivityIntegration connectivityIntegration;

  public ThingServiceImpl(
      ThingRepository thingRepository, ConnectivityIntegration connectivityIntegration) {
    this.thingRepository = thingRepository;
    this.connectivityIntegration = connectivityIntegration;
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
        .findByRoomUidAndUid(roomUid, thingUid)
        .orElseThrow(ThingNotFoundException::new);
  }

  @Override
  public ThingModel createThing(String roomUid, ThingCreateModel thing) {
    ThingEntity entity = new ThingEntity(roomUid, thing.getUid(), thing.getName());
    entity = thingRepository.save(entity);
    return toThingModel(entity);
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

  @Override
  public void deleteThings(String roomUid) {
    thingRepository.deleteAllByRoomUid(roomUid);
  }

  private ThingModel toThingModel(ThingEntity entity) {
    return new ThingModel(entity.getUid(), entity.getName(), entity.getVersion());
  }

  @Override
  @Transactional
  public void deleteThing(String roomUid, String thingUid) {
    thingRepository.deleteByRoomUidAndUid(roomUid, thingUid);
    boolean integrated = connectivityIntegration.onThingDeletion(roomUid, roomUid);
    if (!integrated) {
      throw new InternalServerErrorException();
    }
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
