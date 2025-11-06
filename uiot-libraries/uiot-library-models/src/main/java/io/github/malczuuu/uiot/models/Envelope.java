package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = AccountingWindowEnvelope.class, name = AccountingWindowEnvelope.TYPE),
  @JsonSubTypes.Type(value = AccountingMetricEnvelope.class, name = AccountingMetricEnvelope.TYPE),
  @JsonSubTypes.Type(value = ThingEventsEnvelope.class, name = ThingEventsEnvelope.TYPE),
  @JsonSubTypes.Type(value = RoomCreateEnvelope.class, name = RoomCreateEnvelope.TYPE),
  @JsonSubTypes.Type(value = RoomDeleteEnvelope.class, name = RoomDeleteEnvelope.TYPE),
  @JsonSubTypes.Type(value = ActionExecutionEnvelope.class, name = ActionExecutionEnvelope.TYPE),
  @JsonSubTypes.Type(value = ThingCreateEnvelope.class, name = ThingCreateEnvelope.TYPE),
})
public class Envelope {

  private final String type;

  @JsonCreator
  public Envelope(@JsonProperty("type") String type) {
    this.type = type;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }
}
