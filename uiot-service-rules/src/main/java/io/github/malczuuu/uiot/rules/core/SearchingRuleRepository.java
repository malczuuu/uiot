package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.models.ThingEvent;
import java.util.List;

public interface SearchingRuleRepository {

  List<RuleEntity> findRules(ThingEvent event);
}
