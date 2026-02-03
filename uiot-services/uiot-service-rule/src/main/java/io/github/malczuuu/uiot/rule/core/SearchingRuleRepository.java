package io.github.malczuuu.uiot.rule.core;

import io.github.malczuuu.uiot.model.dto.ThingEvent;
import java.util.List;

public interface SearchingRuleRepository {

  List<RuleEntity> findRules(ThingEvent event);
}
