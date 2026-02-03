package io.github.malczuuu.uiot.thing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingModel {

  private final String uid;
  private final String name;
  private final Long version;

  @JsonCreator
  public ThingModel(
      @JsonProperty("uid") String uid,
      @JsonProperty("name") String name,
      @JsonProperty("version") Long version) {
    this.uid = uid;
    this.name = name;
    this.version = version;
  }

  @JsonProperty("uid")
  public String getUid() {
    return uid;
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
    return "(uid=" + uid + ", name=" + name + ", version=" + version + ")";
  }
}
