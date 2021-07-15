package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;

public class ThingInfo {

  private final Set<String> properties;

  public ThingInfo() {
    this(new HashSet<>());
  }

  @JsonCreator
  public ThingInfo(@JsonProperty("properties") Set<String> properties) {
    this.properties = properties;
  }

  public ThingInfo withNewProperty(String property) {
    Set<String> properties = new HashSet<>(this.properties);
    properties.add(property);
    return new ThingInfo(properties);
  }

  @JsonProperty("properties")
  public Set<String> getProperties() {
    return properties;
  }
}
