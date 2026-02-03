package io.github.malczuuu.uiot.rule.rest;

import io.github.malczuuu.uiot.model.dto.Pagination;
import io.github.malczuuu.uiot.rule.core.RuleService;
import io.github.malczuuu.uiot.rule.model.RuleModel;
import io.github.malczuuu.uiot.rule.model.RulesPage;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/rooms/{room}/rules")
public class RuleController {

  private final RuleService ruleService;

  public RuleController(RuleService ruleService) {
    this.ruleService = ruleService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public RulesPage getRules(
      @PathVariable("room") String room,
      @RequestParam(name = "size", defaultValue = "20") String size) {
    Pagination pagination = Pagination.parseSize(size);
    return ruleService.getRules(room, pagination);
  }

  @GetMapping(path = "/{rule}", produces = MediaType.APPLICATION_JSON_VALUE)
  public RuleModel getRule(@PathVariable("room") String room, @PathVariable("rule") String rule) {
    return ruleService.getRule(room, rule);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RuleModel createRule(
      @PathVariable("room") String room, @RequestBody @Valid RuleModel requestBody) {
    return ruleService.createRule(room, requestBody);
  }

  @DeleteMapping(path = "/{rule}")
  public void deleteRule(@PathVariable("room") String room, @PathVariable("rule") String rule) {
    ruleService.deleteRule(room, rule);
  }
}
