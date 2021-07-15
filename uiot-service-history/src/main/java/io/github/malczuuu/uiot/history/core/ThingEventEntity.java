package io.github.malczuuu.uiot.history.core;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "thing_events")
public class ThingEventEntity {

  public static final String ROOM_FIELD = "room";
  public static final String THING_FIELD = "thing";
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

  @Field(name = "event_id")
  private String eventId;

  @Field(name = ROOM_FIELD)
  private String room;

  @Field(name = THING_FIELD)
  private String thing;

  @Field(name = "property")
  private String property;

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
      String eventId,
      String room,
      String thing,
      String property,
      Long arrivalTime) {
    this.id = id;
    this.name = name;
    this.time = time;
    this.value = value;
    this.valueString = valueString;
    this.valueBoolean = valueBoolean;
    this.valueData = valueData;
    this.unit = unit;
    this.eventId = eventId;
    this.room = room;
    this.thing = thing;
    this.property = property;
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
      String eventId,
      String room,
      String thing,
      String property,
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
        eventId,
        room,
        thing,
        property,
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

  public String getEventId() {
    return eventId;
  }

  public String getRoom() {
    return room;
  }

  public String getThing() {
    return thing;
  }

  public String getProperty() {
    return property;
  }

  public Long getArrivalTime() {
    return arrivalTime;
  }
}
