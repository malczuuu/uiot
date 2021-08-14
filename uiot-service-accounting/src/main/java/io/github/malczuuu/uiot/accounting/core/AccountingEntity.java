package io.github.malczuuu.uiot.accounting.core;

import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "accounting")
public class AccountingEntity {

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field("uuid")
  private String uuid;

  @Field("roomUid")
  private String roomUid;

  @Field("type")
  private String type;

  @Field("tags")
  private Map<String, String> tags;

  @Field("startTime")
  private Long startTime;

  @Field("endTime")
  private Long endTime;

  @Field("value")
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
