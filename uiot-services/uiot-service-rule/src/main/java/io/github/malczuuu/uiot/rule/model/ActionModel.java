package io.github.malczuuu.uiot.rule.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionModel {

  private final String url;

  @JsonCreator
  public ActionModel(@JsonProperty("url") String url) {
    this.url = url;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
}
