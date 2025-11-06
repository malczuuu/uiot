package io.github.malczuuu.uiot.things.core;

import io.github.malczuuu.uiot.problems.NotFoundException;

public class ThingNotFoundException extends NotFoundException {

  public ThingNotFoundException() {
    super("thing not found");
  }
}
