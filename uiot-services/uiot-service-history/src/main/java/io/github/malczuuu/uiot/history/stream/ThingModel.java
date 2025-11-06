package io.github.malczuuu.uiot.history.stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;

public class ThingModel {

  private final Set<String> properties;

  public ThingModel() {
    this(new HashSet<>());
  }

  @JsonCreator
  public ThingModel(@JsonProperty("properties") Set<String> properties) {
    this.properties = properties;
  }

  public ThingModel withNewProperty(String property) {
    Set<String> properties = new HashSet<>(this.properties);
    properties.add(property);
    return new ThingModel(properties);
  }

  @JsonProperty("properties")
  public Set<String> getProperties() {
    return properties;
  }
}
