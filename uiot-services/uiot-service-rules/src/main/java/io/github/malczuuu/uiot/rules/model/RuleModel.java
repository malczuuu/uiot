package io.github.malczuuu.uiot.rules.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleModel {

  private final String uid;
  private final String message;
  private final ConditionModel condition;
  private final ActionModel action;

  @JsonCreator
  public RuleModel(
      @JsonProperty("uid") String uid,
      @JsonProperty("message") String message,
      @JsonProperty("condition") ConditionModel condition,
      @JsonProperty("action") ActionModel action) {
    this.uid = uid;
    this.message = message;
    this.condition = condition;
    this.action = action;
  }

  @JsonProperty("uid")
  public String getUid() {
    return uid;
  }

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("condition")
  public ConditionModel getCondition() {
    return condition;
  }

  @JsonProperty("action")
  public ActionModel getAction() {
    return action;
  }
}
