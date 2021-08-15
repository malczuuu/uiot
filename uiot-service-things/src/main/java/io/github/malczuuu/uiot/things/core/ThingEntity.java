package io.github.malczuuu.uiot.things.core;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "things")
public class ThingEntity {

  public static final String ID_FIELD = "_id";
  public static final String ROOM_FIELD = "roomUid";

  @MongoId
  @Field(ID_FIELD)
  private ObjectId id;

  @Field(ROOM_FIELD)
  private String roomUid;

  @Field("uid")
  private String uid;

  @Field("name")
  private String name;

  @Field("version")
  @Version
  private Long version = null;

  ThingEntity() {}

  public ThingEntity(String roomUid, String uid, String name) {
    this(null, roomUid, uid, name);
  }

  public ThingEntity(ObjectId id, String roomUid, String uid, String name) {
    this.id = id;
    this.roomUid = roomUid;
    this.uid = uid;
    this.name = name;
  }

  public ObjectId getId() {
    return id;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  public Long getVersion() {
    return version;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "(id=" + id + ", uid=" + uid + ", name=" + name + ", version=" + version + ")";
  }
}
