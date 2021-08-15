package io.github.malczuuu.uiot.accounting.stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@JsonPropertyOrder({"type", "tag_entries"})
public class AggregationKey {

  private final String type;
  private final String roomUid;
  private final Map<String, String> tags;

  public AggregationKey(String type, String roomUid, Map<String, String> tags) {
    this.type = type;
    this.roomUid = roomUid;
    this.tags = new HashMap<>(tags);
  }

  @JsonCreator
  public AggregationKey(
      @JsonProperty("type") String type,
      @JsonProperty("room_uid") String roomUid,
      @JsonProperty("tag_entries") List<Entry<String, String>> entries) {
    this.type = type;
    this.roomUid = roomUid;
    this.tags = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("room_uid")
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty("tag_entries")
  public List<Map.Entry<String, String>> getTagEntries() {
    return tags.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toUnmodifiableList());
  }

  @JsonIgnore
  public Map<String, String> getTags() {
    return Collections.unmodifiableMap(tags);
  }
}
