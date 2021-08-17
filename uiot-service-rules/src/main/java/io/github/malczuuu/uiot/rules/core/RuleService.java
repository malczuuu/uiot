package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.rules.model.RuleModel;
import io.github.malczuuu.uiot.rules.model.RulesPage;

public interface RuleService {

  RulesPage getRules(String roomUid, int size);

  RuleModel getRule(String roomUid, String ruleUid);

  RuleModel createRule(String roomUid, RuleModel rule);

  void deleteRule(String roomUid, String ruleUid);

  void deleteRules(String roomUid);
}
