package io.github.malczuuu.uiot.room.core;

import io.github.malczuuu.uiot.model.dto.RoomCreateEvent;
import io.github.malczuuu.uiot.model.dto.RoomDeleteEvent;

public interface RoomEventBroker {

  void publish(RoomCreateEvent event);

  void publish(RoomDeleteEvent event);
}
