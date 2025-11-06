package io.github.malczuuu.uiot.accounting.stream;

import static io.github.malczuuu.uiot.accounting.stream.WindowKey.ROOM_UID_FIELD;
import static io.github.malczuuu.uiot.accounting.stream.WindowKey.TAG_ENTRIES_FIELD;
import static io.github.malczuuu.uiot.accounting.stream.WindowKey.TYPE_FIELD;

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

@JsonPropertyOrder({TYPE_FIELD, ROOM_UID_FIELD, TAG_ENTRIES_FIELD})
public class WindowKey {

  public static final String TYPE_FIELD = "type";
  public static final String ROOM_UID_FIELD = "room_uid";
  public static final String TAG_ENTRIES_FIELD = "tag_entries";

  private final String type;
  private final String roomUid;
  private final Map<String, String> tags;

  public WindowKey(String type, String roomUid, Map<String, String> tags) {
    this.type = type;
    this.roomUid = roomUid;
    this.tags = new HashMap<>(tags);
  }

  @JsonCreator
  public WindowKey(
      @JsonProperty(TYPE_FIELD) String type,
      @JsonProperty(ROOM_UID_FIELD) String roomUid,
      @JsonProperty(TAG_ENTRIES_FIELD) List<Entry<String, String>> entries) {
    this.type = type;
    this.roomUid = roomUid;
    this.tags = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @JsonProperty(TYPE_FIELD)
  public String getType() {
    return type;
  }

  @JsonProperty(ROOM_UID_FIELD)
  public String getRoomUid() {
    return roomUid;
  }

  @JsonProperty(TAG_ENTRIES_FIELD)
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
