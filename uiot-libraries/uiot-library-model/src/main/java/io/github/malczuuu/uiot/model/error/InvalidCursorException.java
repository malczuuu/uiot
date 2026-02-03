package io.github.malczuuu.uiot.model.error;

public class InvalidCursorException extends BadRequestException {

  public InvalidCursorException() {
    super("invalid cursor");
  }
}
