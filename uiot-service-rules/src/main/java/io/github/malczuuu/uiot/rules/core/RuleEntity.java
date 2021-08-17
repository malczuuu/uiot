package io.github.malczuuu.uiot.rules.core;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

public class RuleEntity {

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field("uid")
  private String uid;

  @Field("roomUid")
  private String roomUid;

  @Field("message")
  private String message;

  @Field("condition")
  private ConditionEntity condition;

  @Field("action")
  private ActionEntity action;

  public RuleEntity() {}

  public RuleEntity(
      String uid, String roomUid, String message, ConditionEntity condition, ActionEntity action) {
    this(null, uid, roomUid, message, condition, action);
  }

  public RuleEntity(
      ObjectId id,
      String uid,
      String roomUid,
      String message,
      ConditionEntity condition,
      ActionEntity action) {
    this.id = id;
    this.uid = uid;
    this.roomUid = roomUid;
    this.message = message;
    this.condition = condition;
    this.action = action;
  }

  public ObjectId getId() {
    return id;
  }

  public String getUid() {
    return uid;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getMessage() {
    return message;
  }

  public ConditionEntity getCondition() {
    return condition;
  }

  public ActionEntity getAction() {
    return action;
  }
}
