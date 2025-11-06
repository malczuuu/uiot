package io.github.malczuuu.uiot.rooms.core;

import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.models.RoomDeleteEvent;

public interface RoomEventBroker {

  void publish(RoomCreateEvent event);

  void publish(RoomDeleteEvent event);
}
