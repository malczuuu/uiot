package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingCreateEvent {

  private final String roomUid;
  private final String thingUid;
  private final String thingName;
  private final Long timestamp;

  @JsonCreator
  public ThingCreateEvent(
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("thing_uid") String thingUid,
      @JsonProperty("thing_name") String thingName,
      @JsonProperty("timestamp") Long timestamp) {
    this.roomUid = roomUid;
    this.thingUid = thingUid;
    this.thingName = thingName;
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

  @JsonProperty("thing_name")
  public String getThingName() {
    return thingName;
  }

  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }
}
