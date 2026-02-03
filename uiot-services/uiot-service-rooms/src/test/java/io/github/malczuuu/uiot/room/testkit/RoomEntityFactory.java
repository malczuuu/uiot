package io.github.malczuuu.uiot.room.testkit;

import io.github.malczuuu.uiot.room.core.RoomEntity;
import java.util.ArrayList;
import java.util.List;

public class RoomEntityFactory {

  public List<RoomEntity> generateRooms(int count) {
    List<RoomEntity> rooms = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      String uid = String.format("room-%08d", i);
      String name = String.format("Room %08d", i);
      rooms.add(new RoomEntity(uid, name));
    }
    return rooms;
  }
}
