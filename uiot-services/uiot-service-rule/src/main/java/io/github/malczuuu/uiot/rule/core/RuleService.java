package io.github.malczuuu.uiot.rule.core;

import io.github.malczuuu.uiot.model.dto.Pagination;
import io.github.malczuuu.uiot.model.dto.ThingEvent;
import io.github.malczuuu.uiot.rule.model.RuleModel;
import io.github.malczuuu.uiot.rule.model.RulesPage;
import java.util.List;

public interface RuleService {

  RulesPage getRules(String roomUid, Pagination size);

  RuleModel getRule(String roomUid, String ruleUid);

  RuleModel createRule(String roomUid, RuleModel rule);

  void deleteRule(String roomUid, String ruleUid);

  void deleteRules(String roomUid);

  List<RuleModel> search(ThingEvent thingEvent);
}
