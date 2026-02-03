package io.github.malczuuu.uiot.rule.core;

import org.springframework.data.mongodb.core.mapping.Field;

public class ActionEntity {

  @Field("url")
  private String url;

  public ActionEntity() {}

  public ActionEntity(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
