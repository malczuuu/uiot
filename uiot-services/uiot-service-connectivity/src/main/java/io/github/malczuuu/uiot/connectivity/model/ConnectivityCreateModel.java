package io.github.malczuuu.uiot.connectivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectivityCreateModel {

  private final String password;
  private final Boolean enabled;

  @JsonCreator
  public ConnectivityCreateModel(
      @JsonProperty("password") String password, @JsonProperty("enabled") Boolean enabled) {
    this.password = password;
    this.enabled = enabled;
  }

  @JsonProperty("id")
  public String getPassword() {
    return password;
  }

  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }
}
