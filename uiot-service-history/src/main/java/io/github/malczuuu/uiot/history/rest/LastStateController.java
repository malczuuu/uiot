package io.github.malczuuu.uiot.history.rest;

import io.github.malczuuu.uiot.history.core.LastStateService;
import io.github.malczuuu.uiot.history.model.EventHistory;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/api/rooms/{room}/things/{thing}/last-state")
public class LastStateController {

  private final LastStateService lastStateService;

  public LastStateController(LastStateService lastStateService) {
    this.lastStateService = lastStateService;
  }

  @ApiImplicitParams({@ApiImplicitParam(name = "property_name")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public EventHistory getLastState(
      @PathVariable("room") String room, @PathVariable("thing") String thing) {
    return lastStateService.getLastState(room, thing);
  }

  @ApiIgnore
  @GetMapping(
      params = {"property_name"},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public EventHistory getPropertyLastState(
      @PathVariable("room") String room,
      @PathVariable("thing") String thing,
      @RequestParam("property_name") String propertyName) {
    return lastStateService.getLastState(room, thing, propertyName);
  }
}
