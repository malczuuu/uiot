package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.rules.model.RuleModel;
import io.github.malczuuu.uiot.rules.model.RulesPage;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceImpl implements RuleService {

  @Override
  public RulesPage getRules(String roomUid, int size) {
    return null;
  }

  @Override
  public RuleModel getRule(String roomUid, String ruleUid) {
    return null;
  }

  @Override
  public RuleModel createRule(String roomUid, RuleModel rule) {
    return null;
  }

  @Override
  public void deleteRule(String roomUid, String ruleUid) {}

  @Override
  public void deleteRules(String roomUid) {}
}
