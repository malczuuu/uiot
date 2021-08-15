package io.github.malczuuu.uiot.models.accounting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WindowEventEnvelope {

  private static final String TYPE = "window_event";

  private final WindowEvent windowEvent;

  @JsonCreator
  public WindowEventEnvelope(@JsonProperty(TYPE) WindowEvent windowEvent) {
    this.windowEvent = windowEvent;
  }

  @JsonProperty("type")
  public String getType() {
    return TYPE;
  }

  @JsonProperty(TYPE)
  public WindowEvent getWindowEvent() {
    return windowEvent;
  }
}
