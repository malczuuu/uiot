package io.github.malczuuu.uiot.connectivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectivityModel {

  private final String room;
  private final String thing;
  private final Boolean enabled;
  private final Long version;

  @JsonCreator
  public ConnectivityModel(
      @JsonProperty("room") String room,
      @JsonProperty("thing") String thing,
      @JsonProperty("enabled") Boolean enabled,
      @JsonProperty("version") Long version) {
    this.room = room;
    this.thing = thing;
    this.enabled = enabled;
    this.version = version;
  }

  @JsonProperty("room")
  public String getRoom() {
    return room;
  }

  @JsonProperty("thing")
  public String getThing() {
    return thing;
  }

  @JsonProperty("enabled")
  public boolean isEnabled() {
    return enabled != null && enabled;
  }

  @JsonProperty("version")
  public Long getVersion() {
    return version;
  }
}
