package io.github.malczuuu.uiot.history.core;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

public class ThingEventEntity {

  public static final String ROOM_UID = "room_uid";
  public static final String THING_UID_FIELD = "thing_uid";
  public static final String ARRIVAL_TIME_FIELD = "arrival_time";

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field(name = "name")
  private String name;

  @Field(name = "time")
  private Long time;

  @Field(name = "value")
  private Double value;

  @Field(name = "value_string")
  private String valueString;

  @Field(name = "value_boolean")
  private Boolean valueBoolean;

  @Field(name = "value_data")
  private String valueData;

  @Field(name = "unit")
  private String unit;

  @Field(name = "event_uid")
  private String eventUid;

  @Field(name = ROOM_UID)
  private String roomUid;

  @Field(name = THING_UID_FIELD)
  private String thingUid;

  @Field(name = "property_name")
  private String propertyName;

  @Field(name = ARRIVAL_TIME_FIELD)
  private Long arrivalTime;

  ThingEventEntity() {}

  public ThingEventEntity(
      ObjectId id,
      String name,
      Long time,
      Double value,
      String valueString,
      Boolean valueBoolean,
      String valueData,
      String unit,
      String eventUid,
      String roomUid,
      String thingUid,
      String propertyName,
      Long arrivalTime) {
    this.id = id;
    this.name = name;
    this.time = time;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
    this.valueData = valueData;
    this.unit = unit;
    this.eventUid = eventUid;
    this.roomUid = roomUid;
    this.thingUid = thingUid;
    this.propertyName = propertyName;
    this.arrivalTime = arrivalTime;
  }

  public ThingEventEntity(
      String name,
      Long time,
      Double value,
      String valueString,
      Boolean valueBoolean,
      String valueData,
      String unit,
      String eventUid,
      String roomUid,
      String thingUid,
      String propertyName,
      Long arrivalTime) {
    this(
        null,
        name,
        time,
        value,
        valueString,
        valueBoolean,
        valueData,
        unit,
        eventUid,
        roomUid,
        thingUid,
        propertyName,
        arrivalTime);
  }

  public ObjectId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getTime() {
    return time;
  }

  public Double getValue() {
    return value;
  }

  public String getValueString() {
    return valueString;
  }

  public Boolean getValueBoolean() {
    return valueBoolean;
  }

  public String getValueData() {
    return valueData;
  }

  public String getUnit() {
    return unit;
  }

  public String getEventUid() {
    return eventUid;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getThingUid() {
    return thingUid;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public Long getArrivalTime() {
    return arrivalTime;
  }
}
