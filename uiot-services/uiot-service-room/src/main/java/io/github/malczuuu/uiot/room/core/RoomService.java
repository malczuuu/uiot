package io.github.malczuuu.uiot.room.core;

import io.github.malczuuu.uiot.model.dto.Pagination;
import io.github.malczuuu.uiot.model.dto.RoomCreateEvent;
import io.github.malczuuu.uiot.model.error.InvalidCursorException;
import io.github.malczuuu.uiot.room.model.CursorPage;
import io.github.malczuuu.uiot.room.model.RoomCreateModel;
import io.github.malczuuu.uiot.room.model.RoomModel;
import io.github.malczuuu.uiot.room.model.RoomUpdateModel;

public interface RoomService {

  CursorPage<RoomModel> getRooms(Pagination pagination);

  CursorPage<RoomModel> getRooms(String cursorString) throws InvalidCursorException;

  RoomModel getRoom(String uid) throws RoomNotFoundException;

  void requestRoomCreation(RoomCreateModel ing);

  void createRoom(RoomCreateEvent room);

  RoomModel updateRoom(String uid, RoomUpdateModel room) throws RoomNotFoundException;

  void requestRoomDeletion(String uid);

  void deleteRoom(String uid);
}
