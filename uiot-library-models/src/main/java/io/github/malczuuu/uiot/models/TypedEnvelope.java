package io.github.malczuuu.uiot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.malczuuu.uiot.models.accounting.AccountingMetricEnvelope;
import io.github.malczuuu.uiot.models.accounting.AccountingWindowEnvelope;
import io.github.malczuuu.uiot.models.room.RoomCreateEnvelope;
import io.github.malczuuu.uiot.models.room.RoomDeleteEnvelope;
import io.github.malczuuu.uiot.models.rule.ActionExecutionEnvelope;
import io.github.malczuuu.uiot.models.thing.ThingCreateEnvelope;
import io.github.malczuuu.uiot.models.thing.ThingEventsEnvelope;

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
public class TypedEnvelope {

  private final String type;

  @JsonCreator
  public TypedEnvelope(@JsonProperty("type") String type) {
    this.type = type;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }
}
