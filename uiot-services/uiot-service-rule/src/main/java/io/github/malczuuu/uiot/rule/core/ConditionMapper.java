package io.github.malczuuu.uiot.rule.core;

import io.github.malczuuu.uiot.rule.model.ConditionModel;

public class ConditionMapper {

  public ConditionModel toConditionModel(ConditionEntity condition) {
    return new ConditionModel(
        condition.getThingUids(),
        condition.getPropertyNames(),
        condition.getOperator() != null ? condition.getOperator().toString() : null,
        condition.getValue(),
        condition.getValueString(),
        condition.getValueBoolean());
  }
}
