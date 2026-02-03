package io.github.malczuuu.uiot.rule.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RulesPage {

  private final List<RuleModel> content;

  @JsonCreator
  public RulesPage(@JsonProperty("content") List<RuleModel> content) {
    this.content = content;
  }

  @JsonProperty("content")
  public List<RuleModel> getContent() {
    return content;
  }
}
