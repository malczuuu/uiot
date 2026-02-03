package io.github.malczuuu.uiot.thing.core;

import io.github.malczuuu.uiot.model.error.NotFoundException;

public class ThingNotFoundException extends NotFoundException {

  public ThingNotFoundException() {
    super("thing not found");
  }
}
