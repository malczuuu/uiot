package io.github.malczuuu.uiot.connectivity.core;

import io.github.malczuuu.uiot.problems.NotFoundException;

public class ConnectivityNotFoundException extends NotFoundException {

  public ConnectivityNotFoundException() {
    super("connectivity not found");
  }
}
