package io.github.malczuuu.uiot.thing.rest;

import io.github.malczuuu.uiot.model.dto.Pagination;
import io.github.malczuuu.uiot.thing.core.ThingService;
import io.github.malczuuu.uiot.thing.model.CursorPage;
import io.github.malczuuu.uiot.thing.model.ThingCreateModel;
import io.github.malczuuu.uiot.thing.model.ThingModel;
import io.github.malczuuu.uiot.thing.model.ThingUpdateModel;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/rooms/{room}/things")
public class ThingController {

  private final ThingService thingService;

  public ThingController(ThingService thingService) {
    this.thingService = thingService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public CursorPage<ThingModel> getThings(
      @PathVariable("room") String room,
      @RequestParam(name = "size", defaultValue = "20") String size) {
    Pagination pagination = Pagination.parseSize(size);
    return thingService.getThings(room, pagination);
  }

  @Operation(hidden = true)
  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      params = {"cursor"})
  public CursorPage<ThingModel> getThingsByCursor(
      @PathVariable("room") String room, @RequestParam(name = "cursor") String cursor) {
    return thingService.getThings(room, cursor);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ThingModel createThing(
      @PathVariable("room") String room, @RequestBody @Valid ThingCreateModel requestBody) {
    return thingService.createThing(room, requestBody);
  }

  @GetMapping(path = "/{thing}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ThingModel getThing(
      @PathVariable("room") String room, @PathVariable("thing") String thing) {
    return thingService.getThing(room, thing);
  }

  @PutMapping(
      path = "/{thing}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ThingModel updateThing(
      @PathVariable("room") String room,
      @PathVariable("thing") String thing,
      @RequestBody @Valid ThingUpdateModel requestBody) {
    return thingService.updateThing(room, thing, requestBody);
  }

  @DeleteMapping(path = "/{thing}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteThing(@PathVariable("room") String room, @PathVariable("thing") String thing) {
    thingService.deleteThing(room, thing);
  }
}
