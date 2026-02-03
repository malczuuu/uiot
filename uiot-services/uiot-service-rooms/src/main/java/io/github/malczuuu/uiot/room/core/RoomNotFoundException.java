package io.github.malczuuu.uiot.room.core;

import io.github.malczuuu.uiot.problems.NotFoundException;

public class RoomNotFoundException extends NotFoundException {

  public RoomNotFoundException() {
    super("room not found");
  }
}
