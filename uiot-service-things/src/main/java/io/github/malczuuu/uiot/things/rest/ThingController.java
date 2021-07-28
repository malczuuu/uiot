package io.github.malczuuu.uiot.things.rest;

import io.github.malczuuu.uiot.things.core.ThingService;
import io.github.malczuuu.uiot.things.model.CursorPage;
import io.github.malczuuu.uiot.things.model.ThingCreateModel;
import io.github.malczuuu.uiot.things.model.ThingModel;
import io.github.malczuuu.uiot.things.model.ThingUpdateModel;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/api/rooms/{room}/things")
public class ThingController {

  private static final Logger log = LoggerFactory.getLogger(ThingController.class);

  private final ThingService thingService;

  public ThingController(ThingService thingService) {
    this.thingService = thingService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public CursorPage<ThingModel> getThings(
      @PathVariable("room") String room,
      @RequestParam(name = "size", defaultValue = "20") String size) {
    logThingsApiGetAttempt(room);
    int sizeAsInt = parseSize(size);
    CursorPage<ThingModel> responseBody = thingService.getThings(room, sizeAsInt);
    logThingsApiGet(room, responseBody);
    return responseBody;
  }

  private int parseSize(String size) {
    int sizeAsInt;
    try {
      sizeAsInt = Integer.parseInt(size);
    } catch (NumberFormatException e) {
      sizeAsInt = 20;
    }
    return sizeAsInt;
  }

  private void logThingsApiGetAttempt(String room) {
    log.debug("Attempting to retrieve page of thing(s) on Things API GET, room={}", room);
  }

  private void logThingsApiGet(String room, CursorPage<ThingModel> responseBody) {
    log.info(
        "Retrieved {} of thing(s) on Things API GET, room={}, things={}",
        room,
        responseBody.getContent().size(),
        responseBody.getContent().stream().map(ThingModel::getUid).collect(Collectors.toList()));
  }

  @ApiIgnore
  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      params = {"cursor"})
  public CursorPage<ThingModel> getThingsByCursor(
      @PathVariable("room") String room, @RequestParam(name = "cursor") String cursor) {
    logThingsApiGetAttempt(room);
    CursorPage<ThingModel> responseBody = thingService.getThings(room, cursor);
    logThingsApiGet(room, responseBody);
    return responseBody;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ThingModel createThing(
      @PathVariable("room") String room, @RequestBody @Valid ThingCreateModel requestBody) {
    log.debug("Attempting to create thing on Things API POST, room={}", room);
    ThingModel responseBody = thingService.createThing(room, requestBody);
    log.info("Created thing on Things API POST, room={}, thing={}", room, responseBody.getUid());
    return responseBody;
  }

  @GetMapping(path = "/{thing}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ThingModel getThing(
      @PathVariable("room") String room, @PathVariable("thing") String thing) {
    log.debug("Attempting to retrieve 1 thing on Things API GET, room={}, thing={}", room, thing);
    ThingModel responseBody = thingService.getThing(room, thing);
    log.info("Retrieved 1 thing on Things API GET, room={}, thing={}", room, thing);
    return responseBody;
  }

  @PutMapping(
      path = "/{thing}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ThingModel updateThing(
      @PathVariable("room") String room,
      @PathVariable("thing") String thing,
      @RequestBody @Valid ThingUpdateModel requestBody) {
    log.debug("Attempting to update thing on Things API PUT, room={}, thing={}", room, thing);
    ThingModel responseBody = thingService.updateThing(room, thing, requestBody);
    log.info("Updated thing on Things API PUT, room={}, thing={}", room, thing);
    return responseBody;
  }

  @DeleteMapping(path = "/{thing}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteThing(@PathVariable("room") String room, @PathVariable("thing") String thing) {
    log.debug("Attempting to delete thing on Things API DELETE, room={}, thing={}", room, thing);
    thingService.deleteThing(room, thing);
    log.info("Deleted thing on Things API DELETE, room={}, thing={}", room, thing);
  }
}
