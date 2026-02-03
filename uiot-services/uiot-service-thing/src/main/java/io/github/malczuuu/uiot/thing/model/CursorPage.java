package io.github.malczuuu.uiot.thing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CursorPage<T> {

  private final List<T> content;
  private final Links links;

  @JsonCreator
  public CursorPage(@JsonProperty("content") List<T> content, @JsonProperty("links") Links links) {
    this.content = content;
    this.links = links;
  }

  @JsonProperty("content")
  public List<T> getContent() {
    return content;
  }

  @JsonProperty("links")
  public Links getLinks() {
    return links;
  }

  public static class Links {

    private final String nextPath;

    @JsonCreator
    public Links(@JsonProperty("nextPath") String nextPath) {
      this.nextPath = nextPath;
    }

    @JsonProperty("nextPath")
    public String getNextPath() {
      return nextPath;
    }
  }
}
