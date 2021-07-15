package io.github.malczuuu.uiot.schema.event.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingDeleteEvent {

  private final String roomUid;
  private final String thingUid;
  private final Long timestamp;

  @JsonCreator
  public ThingDeleteEvent(
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("thing_uid") String thingUid,
      @JsonProperty("timestamp") Long timestamp) {
    this.roomUid = roomUid;
    this.thingUid = thingUid;
    this.timestamp = timestamp;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("thing_uid")
  public String getThingUid() {
    return thingUid;
  }

  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }
}
