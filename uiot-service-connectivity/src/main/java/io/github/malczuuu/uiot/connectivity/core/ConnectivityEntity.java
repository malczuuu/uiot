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

  @Field("room")
  private String room;

  @Field("thing")
  private String thing;

  @Field("password")
  private String password;

  @Field("enabled")
  private Boolean enabled;

  @Version
  @Field("version")
  private Long version;

  ConnectivityEntity() {}

  public ConnectivityEntity(
      ObjectId id, String room, String thing, String password, Boolean enabled) {
    this(id, room, thing, password, enabled, null);
  }

  public ConnectivityEntity(
      ObjectId id, String room, String thing, String password, Boolean enabled, Long version) {
    this.id = id;
    this.room = room;
    this.thing = thing;
    this.password = password;
    this.enabled = enabled;
    this.version = version;
  }

  public ConnectivityEntity(String room, String thing, String password, Boolean enabled) {
    this(null, room, thing, password, enabled);
  }

  public ObjectId getId() {
    return id;
  }

  public String getRoom() {
    return room;
  }

  public String getThing() {
    return thing;
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
