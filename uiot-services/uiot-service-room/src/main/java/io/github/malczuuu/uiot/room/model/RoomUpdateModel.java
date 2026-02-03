package io.github.malczuuu.uiot.room.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomUpdateModel {

  private final String name;
  private final Long version;

  @JsonCreator
  public RoomUpdateModel(@JsonProperty("name") String name, @JsonProperty("version") Long version) {
    this.name = name;
    this.version = version;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("version")
  public Long getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return "(name=" + name + ", version=" + version + ")";
  }
}
