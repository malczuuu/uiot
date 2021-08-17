package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.problems.NotFoundException;

public class RuleNotFoundException extends NotFoundException {

  public RuleNotFoundException() {
    super("rule not found");
  }
}
