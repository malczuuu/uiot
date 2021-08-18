package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;

public interface StorageService {

  void createStorage(RoomCreateEvent roomCreate);

  void deleteStorage(RoomDeleteEvent roomDelete);
}
