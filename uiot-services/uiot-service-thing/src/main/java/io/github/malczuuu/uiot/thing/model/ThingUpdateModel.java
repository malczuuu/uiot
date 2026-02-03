package io.github.malczuuu.uiot.thing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ThingUpdateModel {

  private final String name;
  private final Long version;

  @JsonCreator
  public ThingUpdateModel(
      @JsonProperty("name") String name, @JsonProperty("version") Long version) {
    this.name = name;
    this.version = version;
  }

  @NotNull
  @Pattern(
      regexp = "^[0-9a-zA-Z].*[0-9a-zA-Z]$",
      message = "must begin and end with an alphanumeric character")
  @Size(min = 1, max = 64)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @NotNull
  @JsonProperty("version")
  public Long getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return "(name=" + name + ", version=" + version + ")";
  }
}
