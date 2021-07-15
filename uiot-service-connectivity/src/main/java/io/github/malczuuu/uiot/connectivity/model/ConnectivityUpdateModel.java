package io.github.malczuuu.uiot.connectivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectivityUpdateModel {

  private final Boolean enabled;
  private final Long version;

  @JsonCreator
  public ConnectivityUpdateModel(
      @JsonProperty("enabled") Boolean enabled, @JsonProperty("version") Long version) {
    this.enabled = enabled;
    this.version = version;
  }

  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  @JsonProperty("version")
  public Long getVersion() {
    return version;
  }
}
