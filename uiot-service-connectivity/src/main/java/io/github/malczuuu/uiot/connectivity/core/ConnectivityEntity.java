package io.github.malczuuu.uiot.connectivity.core;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "connectivity")
public class ConnectivityEntity {

  @MongoId
  @Field("_id")
  private ObjectId id;

  @Field("roomUid")
  private String roomUid;

  @Field("thingUid")
  private String thingUid;

  @Field("password")
  private String password;

  @Field("enabled")
  private Boolean enabled;

  @Version
  @Field("version")
  private Long version;

  ConnectivityEntity() {}

  public ConnectivityEntity(
      ObjectId id, String roomUid, String thingUid, String password, Boolean enabled) {
    this(id, roomUid, thingUid, password, enabled, null);
  }

  public ConnectivityEntity(
      ObjectId id,
      String roomUid,
      String thingUid,
      String password,
      Boolean enabled,
      Long version) {
    this.id = id;
    this.roomUid = roomUid;
    this.thingUid = thingUid;
    this.password = password;
    this.enabled = enabled;
    this.version = version;
  }

  public ConnectivityEntity(String roomUid, String thingUid, String password, Boolean enabled) {
    this(null, roomUid, thingUid, password, enabled);
  }

  public ObjectId getId() {
    return id;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getThingUid() {
    return thingUid;
  }

  public String getPassword() {
    return password;
  }

  public boolean isEnabled() {
    return enabled != null && enabled;
  }

  public Long getVersion() {
    return version;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
