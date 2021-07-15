package io.github.malczuuu.uiot.things.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ThingCreateModel {

  private final String uid;
  private final String name;

  @JsonCreator
  public ThingCreateModel(@JsonProperty("uid") String uid, @JsonProperty("name") String name) {
    this.uid = uid;
    this.name = name;
  }

  @NotNull
  @Pattern(
      regexp = "^[0-9a-zA-Z\\-_]*$",
      message = "must consist of alphanumeric characters, hyphen (-) and underscore (_)")
  @Size(min = 1, max = 64)
  @JsonProperty("uid")
  public String getUid() {
    return uid;
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

  @Override
  public String toString() {
    return "(uid=" + uid + ", name=" + name + ")";
  }
}
