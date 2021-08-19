package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", ActionExecutionEnvelope.TYPE})
public class ActionExecutionEnvelope extends Envelope {

  public static final String TYPE = "action_execution";

  private final ActionExecutionEvent actionExecution;

  @JsonCreator
  public ActionExecutionEnvelope(@JsonProperty(TYPE) ActionExecutionEvent actionExecution) {
    super(TYPE);
    this.actionExecution = actionExecution;
  }

  @JsonProperty(TYPE)
  public ActionExecutionEvent getActionExecution() {
    return actionExecution;
  }
}
