package io.github.malczuuu.uiot.rules.core;

import io.github.malczuuu.uiot.models.ThingEvent;
import java.util.List;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

public class SearchingRuleRepositoryImpl implements SearchingRuleRepository {

  private final MongoOperations mongoOperations;

  public SearchingRuleRepositoryImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<RuleEntity> findRules(ThingEvent event) {
    CriteriaDefinition criteria =
        Criteria.where(RuleEntity.CONDITION_THING_UIDS)
            .is(event.getThing())
            .and(RuleEntity.CONDITION_PROPERTY_NAMES)
            .is(event.getProperty())
            .andOperator(
                new Criteria()
                    .orOperator(
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.EQ.toString())
                            .and(RuleEntity.CONDITION_VALUE)
                            .is(event.getValue()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.NEQ.toString())
                            .and(RuleEntity.CONDITION_VALUE)
                            .ne(event.getValue()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.GT.toString())
                            .and(RuleEntity.CONDITION_VALUE)
                            .lt(event.getValue()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.LT.toString())
                            .and(RuleEntity.CONDITION_VALUE)
                            .gt(event.getValue()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.GTE.toString())
                            .and(RuleEntity.CONDITION_VALUE)
                            .lte(event.getValue()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.LTE.toString())
                            .and(RuleEntity.CONDITION_VALUE)
                            .gte(event.getValue()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.EQ.toString())
                            .and(RuleEntity.CONDITION_VALUE_STRING)
                            .is(event.getValueString()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.NEQ.toString())
                            .and(RuleEntity.CONDITION_VALUE_STRING)
                            .ne(event.getValueString()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.EQ.toString())
                            .and(RuleEntity.CONDITION_VALUE_BOOLEAN)
                            .is(event.getValueBoolean()),
                        new Criteria(RuleEntity.CONDITION_OPERATOR)
                            .is(OperatorEntity.NEQ.toString())
                            .and(RuleEntity.CONDITION_VALUE_BOOLEAN)
                            .ne(event.getValueBoolean())));
    Query query = Query.query(criteria);
    return mongoOperations.find(query, RuleEntity.class);
  }
}
