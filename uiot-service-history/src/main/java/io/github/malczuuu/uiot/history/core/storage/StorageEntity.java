package io.github.malczuuu.uiot.history.core.storage;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "storages")
public class StorageEntity {

  public static final String ROOM_UID_FIELD = "room_uid";
  public static final String STORAGE_NAME_FIELD = "storage_name";

  @MongoId(targetType = FieldType.OBJECT_ID)
  private ObjectId id;

  @Field(name = ROOM_UID_FIELD)
  private String roomUid;

  @Field(name = STORAGE_NAME_FIELD)
  private String storageName;

  public StorageEntity() {}

  public StorageEntity(String roomUid, String storageName) {
    this(null, roomUid, storageName);
  }

  public StorageEntity(ObjectId id, String roomUid, String storageName) {
    this.id = id;
    this.roomUid = roomUid;
    this.storageName = storageName;
  }

  public ObjectId getId() {
    return id;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getStorageName() {
    return storageName;
  }
}
