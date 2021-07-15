package io.github.malczuuu.uiot.rooms.core;

import io.github.malczuuu.uiot.rooms.model.CursorPage;
import io.github.malczuuu.uiot.rooms.model.RoomCreateModel;
import io.github.malczuuu.uiot.rooms.model.RoomModel;
import io.github.malczuuu.uiot.rooms.model.RoomUpdateModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface RoomService {

  CursorPage<RoomModel> getRooms(int size);

  CursorPage<RoomModel> getRooms(String cursorString) throws InvalidCursorException;

  RoomModel getRoom(String uid) throws RoomNotFoundException;

  RoomModel requestRoomCreation(RoomCreateModel ing);

  void createRoom(RoomModel room);

  RoomModel updateRoom(String uid, RoomUpdateModel room) throws RoomNotFoundException;

  void requestRoomDeletion(String uid);

  void deleteRoom(String uid);

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "room not found")
  class RoomNotFoundException extends RuntimeException {}

  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid cursor")
  class InvalidCursorException extends RuntimeException {}
}
