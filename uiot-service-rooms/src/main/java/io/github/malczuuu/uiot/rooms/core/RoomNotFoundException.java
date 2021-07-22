package io.github.malczuuu.uiot.rooms.core;

import io.github.malczuuu.uiot.http.errors.NotFoundException;

public class RoomNotFoundException extends NotFoundException {

  public RoomNotFoundException() {
    super("room not found");
  }
}
