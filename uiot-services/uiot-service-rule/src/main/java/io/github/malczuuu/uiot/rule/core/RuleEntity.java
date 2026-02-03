package io.github.malczuuu.uiot.rule.core;

import static io.github.malczuuu.uiot.rule.core.ConditionEntity.OPERATOR;
import static io.github.malczuuu.uiot.rule.core.ConditionEntity.PROPERTY_NAMES;
import static io.github.malczuuu.uiot.rule.core.ConditionEntity.THING_UIDS;
import static io.github.malczuuu.uiot.rule.core.ConditionEntity.VALUE;
import static io.github.malczuuu.uiot.rule.core.ConditionEntity.VALUE_BOOLEAN;
import static io.github.malczuuu.uiot.rule.core.ConditionEntity.VALUE_STRING;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "rules")
public class RuleEntity {

  public static final String UID = "uid";
  public static final String ROOM_UID = "roomUid";
  public static final String MESSAGE = "message";
  public static final String CONDITION = "condition";
  public static final String ACTION = "action";

  public static final String CONDITION_THING_UIDS = "condition." + THING_UIDS;
  public static final String CONDITION_PROPERTY_NAMES = "condition." + PROPERTY_NAMES;
  public static final String CONDITION_OPERATOR = "condition." + OPERATOR;
  public static final String CONDITION_VALUE = "condition." + VALUE;
  public static final String CONDITION_VALUE_STRING = "condition." + VALUE_STRING;
  public static final String CONDITION_VALUE_BOOLEAN = "condition." + VALUE_BOOLEAN;

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field(UID)
  private String uid;

  @Field(ROOM_UID)
  private String roomUid;

  @Field(MESSAGE)
  private String message;

  @Field(CONDITION)
  private ConditionEntity condition;

  @Field(ACTION)
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
