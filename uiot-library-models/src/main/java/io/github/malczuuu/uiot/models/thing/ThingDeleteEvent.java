package io.github.malczuuu.uiot.models.thing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingDeleteEvent {

  private final String roomUid;
  private final String thingUid;
  private final Long time;

  @JsonCreator
  public ThingDeleteEvent(
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("thing_uid") String thingUid,
      @JsonProperty("time") Long time) {
    this.roomUid = roomUid;
    this.thingUid = thingUid;
    this.time = time;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("thing_uid")
  public String getThingUid() {
    return thingUid;
  }

  @JsonProperty("time")
  public Long getTime() {
    return time;
  }
}
