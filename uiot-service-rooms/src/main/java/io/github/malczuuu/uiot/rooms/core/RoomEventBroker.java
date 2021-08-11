package io.github.malczuuu.uiot.rooms.core;

import io.github.malczuuu.uiot.models.room.RoomCreateEvent;
import io.github.malczuuu.uiot.models.room.RoomDeleteEvent;

public interface RoomEventBroker {

  void publish(RoomCreateEvent event);

  void publish(RoomDeleteEvent event);
}
