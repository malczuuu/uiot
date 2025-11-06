package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.rules.model.ActionModel;

public class ActionMapper {

  public ActionModel toActionModel(ActionEntity action) {
    return new ActionModel(action.getUrl());
  }
}
