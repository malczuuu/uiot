package io.github.malczuuu.uiot.connectivity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordUpdateModel {

  private final String password;
  private final Long version;

  @JsonCreator
  public PasswordUpdateModel(
      @JsonProperty("password") String password, @JsonProperty("version") Long version) {
    this.password = password;
    this.version = version;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  @JsonProperty("version")
  public Long getVersion() {
    return version;
  }
}
