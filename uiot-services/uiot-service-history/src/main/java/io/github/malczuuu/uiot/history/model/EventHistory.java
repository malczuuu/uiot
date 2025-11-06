package io.github.malczuuu.uiot.history.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class EventHistory {

  private final List<HistoryRecord> content;

  public EventHistory(List<HistoryRecord> content) {
    this.content = content;
  }

  @JsonIgnore
  public Stream<HistoryRecord> stream() {
    return getContent().stream();
  }

  @JsonProperty("content")
  public List<HistoryRecord> getContent() {
    return Collections.unmodifiableList(content);
  }
}
