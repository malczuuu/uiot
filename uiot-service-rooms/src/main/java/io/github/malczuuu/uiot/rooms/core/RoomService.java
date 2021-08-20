package io.github.malczuuu.uiot.rooms.core;

import io.github.malczuuu.uiot.models.RoomCreateEvent;
import io.github.malczuuu.uiot.problems.InvalidCursorException;
import io.github.malczuuu.uiot.rooms.model.CursorPage;
import io.github.malczuuu.uiot.rooms.model.RoomCreateModel;
import io.github.malczuuu.uiot.rooms.model.RoomModel;
import io.github.malczuuu.uiot.rooms.model.RoomUpdateModel;

public interface RoomService {

  CursorPage<RoomModel> getRooms(int size);

  CursorPage<RoomModel> getRooms(String cursorString) throws InvalidCursorException;

  RoomModel getRoom(String uid) throws RoomNotFoundException;

  void requestRoomCreation(RoomCreateModel ing);

  void createRoom(RoomCreateEvent room);

  RoomModel updateRoom(String uid, RoomUpdateModel room) throws RoomNotFoundException;

  void requestRoomDeletion(String uid);

  void deleteRoom(String uid);
}
