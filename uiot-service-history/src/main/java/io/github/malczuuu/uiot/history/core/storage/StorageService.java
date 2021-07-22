package io.github.malczuuu.uiot.history.core.storage;

import io.github.malczuuu.uiot.schema.event.room.RoomCreateEvent;
import io.github.malczuuu.uiot.schema.event.room.RoomDeleteEvent;

public interface StorageService {

  void createStorage(RoomCreateEvent roomCreate);

  void deleteStorage(RoomDeleteEvent roomDelete);
}
