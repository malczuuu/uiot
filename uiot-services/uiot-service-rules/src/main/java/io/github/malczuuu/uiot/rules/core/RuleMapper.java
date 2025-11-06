package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.rules.model.RuleModel;

public class RuleMapper {

  private final ConditionMapper conditionMapper = new ConditionMapper();
  private final ActionMapper actionMapper = new ActionMapper();

  public RuleModel toRuleModel(RuleEntity rule) {
    return new RuleModel(
        rule.getUid(),
        rule.getMessage(),
        rule.getCondition() != null ? conditionMapper.toConditionModel(rule.getCondition()) : null,
        rule.getAction() != null ? actionMapper.toActionModel(rule.getAction()) : null);
  }
}
