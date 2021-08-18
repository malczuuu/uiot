package io.github.malczuuu.uiot.models.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.malczuuu.uiot.models.TypedEnvelope;

@JsonPropertyOrder({"type", ActionExecutionEnvelope.TYPE})
public class ActionExecutionEnvelope extends TypedEnvelope {

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
