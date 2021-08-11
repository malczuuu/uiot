package io.github.malczuuu.uiot.problems;

public class InvalidCursorException extends BadRequestException {

  public InvalidCursorException() {
    super("invalid cursor");
  }
}
