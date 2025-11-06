package io.github.malczuuu.uiot.history.rest;

import io.github.malczuuu.uiot.history.core.HistoryService;
import io.github.malczuuu.uiot.history.model.EventHistory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/rooms/{room}/history")
public class HistoryController {

  private final HistoryService historyService;

  public HistoryController(HistoryService historyService) {
    this.historyService = historyService;
  }

  @Parameters({@Parameter(name = "thing_uid")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public EventHistory getHistory(
      @PathVariable("room") String roomUid, @RequestParam("thing_uid") String thingUid) {
    return historyService.getEventHistory(roomUid, 100);
  }
  //
  //  @Operation(hidden = true)
  //  @GetMapping(
  //      params = {"thing_uid"},
  //      produces = MediaType.APPLICATION_JSON_VALUE)
  //  public EventHistory getThingHistory(
  //      @PathVariable("room") String roomUid, @RequestParam("thing_uid") String thingUid) {
  //    return historyService.getThingEventHistory(roomUid, thingUid, 100);
  //  }
}
