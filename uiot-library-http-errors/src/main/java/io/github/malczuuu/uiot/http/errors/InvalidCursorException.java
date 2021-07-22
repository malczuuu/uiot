package io.github.malczuuu.uiot.http.errors;

public class InvalidCursorException extends BadRequestException {

  public InvalidCursorException() {
    super("invalid cursor");
  }
}
