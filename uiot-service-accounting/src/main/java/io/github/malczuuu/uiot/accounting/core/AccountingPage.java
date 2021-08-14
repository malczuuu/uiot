package io.github.malczuuu.uiot.accounting.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountingPage {

  private final List<AccountingRecord> content;

  @JsonCreator
  public AccountingPage(@JsonProperty("content") List<AccountingRecord> content) {
    this.content = new ArrayList<>(content);
  }

  @JsonProperty("content")
  public List<AccountingRecord> getContent() {
    return Collections.unmodifiableList(content);
  }
}
