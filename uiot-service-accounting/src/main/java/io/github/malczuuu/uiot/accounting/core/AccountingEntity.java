package io.github.malczuuu.uiot.accounting.core;

import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "accounting")
public class AccountingEntity {

  public static final String UUID = "uuid";
  public static final String ROOM_UID = "roomUid";
  public static final String TYPE = "type";
  public static final String TAGS = "tags";
  public static final String START_TIME = "startTime";
  public static final String END_TIME = "endTime";
  public static final String VALUE = "value";

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field(UUID)
  private String uuid;

  @Field(ROOM_UID)
  private String roomUid;

  @Field(TYPE)
  private String type;

  @Field(TAGS)
  private Map<String, String> tags;

  @Field(START_TIME)
  private Long startTime;

  @Field(END_TIME)
  private Long endTime;

  @Field(VALUE)
  private Double value;

  public AccountingEntity() {}

  public AccountingEntity(
      String uuid,
      String roomUid,
      String type,
      Map<String, String> tags,
      Long startTime,
      Long endTime,
      Double value) {
    this(null, uuid, roomUid, type, tags, startTime, endTime, value);
  }

  public AccountingEntity(
      ObjectId id,
      String uuid,
      String roomUid,
      String type,
      Map<String, String> tags,
      Long startTime,
      Long endTime,
      Double value) {
    this.id = id;
    this.uuid = uuid;
    this.roomUid = roomUid;
    this.type = type;
    this.tags = tags;
    this.startTime = startTime;
    this.endTime = endTime;
    this.value = value;
  }

  public ObjectId getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getType() {
    return type;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public Long getStartTime() {
    return startTime;
  }

  public Long getEndTime() {
    return endTime;
  }

  public Double getValue() {
    return value;
  }
}
