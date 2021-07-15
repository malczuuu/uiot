package io.github.malczuuu.uiot.rooms.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = RoomEntity.ROOMS_COLLECTION)
public class RoomEntity {

  public static final String ROOMS_COLLECTION = "rooms";

  public static final String ID_FIELD = "_id";
  public static final String UID_FIELD = "uid";
  public static final String NAME_FIELD = "name";
  public static final String VERSION_FIELD = "version";

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field(name = UID_FIELD)
  private String uid;

  @Field(name = NAME_FIELD)
  private String name;

  @Field(name = VERSION_FIELD)
  private Long version;

  public RoomEntity() {}

  public RoomEntity(String uid, String name) {
    this(null, uid, name, null);
  }

  public RoomEntity(ObjectId id, String uid, String name, Long version) {
    this.id = id;
    this.uid = uid;
    this.name = name;
    this.version = version;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public ObjectId getId() {
    return id;
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

  @Override
  public String toString() {
    return "(id=" + id + ", uid=" + uid + ", name=" + name + ", version=" + version + ")";
  }
}
