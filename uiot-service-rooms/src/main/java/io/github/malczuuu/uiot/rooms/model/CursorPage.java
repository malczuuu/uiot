package io.github.malczuuu.uiot.rooms.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public class CursorPage<T> {

  public static <T> CursorPage<T> empty() {
    return new CursorPage<>(Collections.emptyList(), null);
  }

  private final List<T> content;
  private final Links links;

  @JsonCreator
  public CursorPage(@JsonProperty("content") List<T> content, @JsonProperty("links") Links links) {
    this.content = content;
    this.links = links;
  }

  @JsonProperty("content")
  public List<T> getContent() {
    return Collections.unmodifiableList(content);
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
