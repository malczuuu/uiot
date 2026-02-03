package io.github.malczuuu.uiot.rule.core;

import io.github.malczuuu.uiot.model.error.NotFoundException;

public class RuleNotFoundException extends NotFoundException {

  public RuleNotFoundException() {
    super("rule not found");
  }
}
