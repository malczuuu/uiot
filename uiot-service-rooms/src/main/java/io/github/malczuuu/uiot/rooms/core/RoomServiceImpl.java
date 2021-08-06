package io.github.malczuuu.uiot.rooms.core;

import io.github.malczuuu.uiot.http.errors.InvalidCursorException;
import io.github.malczuuu.uiot.rooms.entity.RoomEntity;
import io.github.malczuuu.uiot.rooms.entity.RoomRepository;
import io.github.malczuuu.uiot.rooms.model.CursorPage;
import io.github.malczuuu.uiot.rooms.model.RoomCreateModel;
import io.github.malczuuu.uiot.rooms.model.RoomModel;
import io.github.malczuuu.uiot.rooms.model.RoomUpdateModel;
import io.github.malczuuu.uiot.schema.event.room.RoomCreateEvent;
import io.github.malczuuu.uiot.schema.event.room.RoomDeleteEvent;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;
  private final RoomEventBroker roomEventBroker;
  private final Clock clock;

  public RoomServiceImpl(
      RoomRepository roomRepository, RoomEventBroker roomEventBroker, Clock clock) {
    this.roomRepository = roomRepository;
    this.roomEventBroker = roomEventBroker;
    this.clock = clock;
  }

  @Override
  public CursorPage<RoomModel> getRooms(int size) {
    List<RoomEntity> entities = roomRepository.findWithoutCursor(size);
    return toNextPage(entities, size);
  }

  private CursorPage<RoomModel> toNextPage(List<RoomEntity> entities, int size) {
    String cursor =
        entities.size() == size ? entities.get(size - 1).getId().toHexString() + "." + size : null;
    return new CursorPage<>(
        entities.stream().map(this::toRoomModel).collect(Collectors.toList()),
        cursor != null ? new CursorPage.Links("/api/rooms?cursor=" + cursor) : null);
  }

  @Override
  public CursorPage<RoomModel> getRooms(String cursorString) throws InvalidCursorException {
    Cursor cursor = Cursor.from(cursorString).orElseThrow(InvalidCursorException::new);
    List<RoomEntity> entities = roomRepository.findWithCursor(cursor.id, cursor.size);
    return toNextPage(entities, cursor.size);
  }

  @Override
  public RoomModel getRoom(String uid) throws RoomNotFoundException {
    RoomEntity entity = fetchRoom(uid);
    return toRoomModel(entity);
  }

  private RoomEntity fetchRoom(String uid) {
    return roomRepository.findByUid(uid).orElseThrow(RoomNotFoundException::new);
  }

  @Override
  public RoomModel requestRoomCreation(RoomCreateModel room) {
    RoomModel model = new RoomModel(room.getUid(), room.getName(), null);
    long time = nowAsNano();
    roomEventBroker.publish(new RoomCreateEvent(model.getUid(), model.getName(), time));
    return model;
  }

  @Override
  public void createRoom(RoomCreateEvent room) {
    RoomEntity entity = new RoomEntity(room.getRoomUid(), room.getRoomName());
    try {
      roomRepository.save(entity);
    } catch (DuplicateKeyException ignored) {
    }
  }

  @Override
  public RoomModel updateRoom(String uid, RoomUpdateModel room) throws RoomNotFoundException {
    RoomEntity entity = fetchRoom(uid);
    entity.setName(room.getName());
    entity.setVersion(room.getVersion());
    entity = roomRepository.save(entity);
    return toRoomModel(entity);
  }

  private RoomModel toRoomModel(RoomEntity entity) {
    return new RoomModel(entity.getUid(), entity.getName(), entity.getVersion());
  }

  @Override
  public void requestRoomDeletion(String uid) {
    if (roomRepository.existsByUid(uid)) {
      long time = nowAsNano();
      roomEventBroker.publish(new RoomDeleteEvent(uid, time));
    }
  }

  private long nowAsNano() {
    Instant timestamp = clock.instant();
    return timestamp.getEpochSecond() * 1000_000_000L + timestamp.getNano();
  }

  @Override
  public void deleteRoom(String uid) {
    roomRepository.deleteByUid(uid);
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
