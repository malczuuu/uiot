package io.github.malczuuu.uiot.rule.core;

import io.github.malczuuu.uiot.rule.model.ActionModel;

public class ActionMapper {

  public ActionModel toActionModel(ActionEntity action) {
    return new ActionModel(action.getUrl());
  }
}
