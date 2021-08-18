package io.github.malczuuu.uiot.models.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", ActionExecutionEnvelope.TYPE})
public class ActionExecutionEnvelope {

  public static final String TYPE = "action_execution";

  private final ActionExecutionEvent actionExecution;

  @JsonCreator
  public ActionExecutionEnvelope(@JsonProperty(TYPE) ActionExecutionEvent actionExecution) {
    this.actionExecution = actionExecution;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public ActionExecutionEvent getActionExecution() {
    return actionExecution;
  }
}
