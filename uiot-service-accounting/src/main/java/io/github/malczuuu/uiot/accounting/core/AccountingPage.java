package io.github.malczuuu.uiot.accounting.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountingPage {

  private final List<AccountingModel> content;

  @JsonCreator
  public AccountingPage(@JsonProperty("content") List<AccountingModel> content) {
    this.content = new ArrayList<>(content);
  }

  @JsonProperty("content")
  public List<AccountingModel> getContent() {
    return Collections.unmodifiableList(content);
  }
}
