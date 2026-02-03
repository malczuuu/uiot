package io.github.malczuuu.uiot.history.core;

import io.github.malczuuu.uiot.model.dto.RoomCreateEvent;
import io.github.malczuuu.uiot.model.dto.RoomDeleteEvent;

public interface StorageService {

  void createStorage(RoomCreateEvent roomCreate);

  void deleteStorage(RoomDeleteEvent roomDelete);
}
