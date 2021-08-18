package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.models.thing.ThingEvent;
import java.util.List;

public interface SearchingRuleRepository {

  List<RuleEntity> findRules(ThingEvent event);
}
