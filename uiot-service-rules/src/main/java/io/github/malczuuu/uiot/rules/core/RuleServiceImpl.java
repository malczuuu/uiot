package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.rules.model.ConditionModel;
import io.github.malczuuu.uiot.rules.model.RuleModel;
import io.github.malczuuu.uiot.rules.model.RulesPage;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceImpl implements RuleService {

  private final RuleRepository ruleRepository;

  private final RuleMapper ruleMapper = new RuleMapper();

  public RuleServiceImpl(RuleRepository ruleRepository) {
    this.ruleRepository = ruleRepository;
  }

  @Override
  public RulesPage getRules(String roomUid, int size) {
    Page<RuleEntity> rules = ruleRepository.findAllByRoomUid(roomUid, PageRequest.of(0, size));
    return new RulesPage(rules.stream().map(ruleMapper::toRuleModel).collect(Collectors.toList()));
  }

  @Override
  public RuleModel getRule(String roomUid, String ruleUid) {
    return ruleRepository
        .findByRoomUidAndUid(roomUid, ruleUid)
        .map(ruleMapper::toRuleModel)
        .orElseThrow(RuleNotFoundException::new);
  }

  @Override
  public RuleModel createRule(String roomUid, RuleModel rule) {
    RuleEntity entity =
        new RuleEntity(
            UUID.randomUUID().toString().replace("-", ""),
            roomUid,
            rule.getMessage(),
            rule.getCondition() != null ? toEntity(rule.getCondition()) : null,
            rule.getAction() != null ? new ActionEntity(rule.getAction().getUrl()) : null);
    return null;
  }

  private ConditionEntity toEntity(ConditionModel condition) {
    if (condition.getValue() != null) {
      return new ConditionEntity(
          condition.getThingUids(),
          condition.getPropertyNames(),
          OperatorEntity.valueOf(condition.getOperator()),
          condition.getValue());
    } else if (condition.getValueString() != null) {
      return new ConditionEntity(
          condition.getThingUids(),
          condition.getPropertyNames(),
          OperatorEntity.valueOf(condition.getOperator()),
          condition.getValueString());
    } else if (condition.getValueBoolean() != null) {
      return new ConditionEntity(
          condition.getThingUids(),
          condition.getPropertyNames(),
          OperatorEntity.valueOf(condition.getOperator()),
          condition.getValueBoolean());
    } else {
      return new ConditionEntity(
          condition.getThingUids(),
          condition.getPropertyNames(),
          OperatorEntity.valueOf(condition.getOperator()),
          0.0);
    }
  }

  @Override
  public void deleteRule(String roomUid, String ruleUid) {
    ruleRepository.deleteByRoomUidAndUid(roomUid, ruleUid);
  }

  @Override
  public void deleteRules(String roomUid) {
    ruleRepository.deleteByRoomUid(roomUid);
  }
}
