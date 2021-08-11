package io.github.malczuuu.uiot.history.core.storage;

import io.github.malczuuu.uiot.models.room.RoomCreateEvent;
import io.github.malczuuu.uiot.models.room.RoomDeleteEvent;

public interface StorageService {

  void createStorage(RoomCreateEvent roomCreate);

  void deleteStorage(RoomDeleteEvent roomDelete);
}
