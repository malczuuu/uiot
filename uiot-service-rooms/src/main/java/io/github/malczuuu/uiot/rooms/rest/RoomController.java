package io.github.malczuuu.uiot.rooms.rest;

import io.github.malczuuu.uiot.rooms.core.RoomService;
import io.github.malczuuu.uiot.rooms.model.CursorPage;
import io.github.malczuuu.uiot.rooms.model.RoomCreateModel;
import io.github.malczuuu.uiot.rooms.model.RoomModel;
import io.github.malczuuu.uiot.rooms.model.RoomUpdateModel;
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
@RequestMapping(path = "/api/rooms")
public class RoomController {

  private static final Logger log = LoggerFactory.getLogger(RoomController.class);

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public CursorPage<RoomModel> getRooms(
      @RequestParam(name = "size", defaultValue = "20") String size) {
    int sizeAsInt = parseSize(size);
    CursorPage<RoomModel> responseBody = roomService.getRooms(sizeAsInt);
    logRoomsApiGet(responseBody);
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

  private void logRoomsApiGet(CursorPage<RoomModel> responseBody) {
    log.info(
        "Retrieved {} of room(s) on Rooms API GET, rooms={}",
        responseBody.getContent().size(),
        responseBody.getContent().stream().map(RoomModel::getUid).collect(Collectors.toList()));
  }

  @ApiIgnore
  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      params = {"cursor"})
  public CursorPage<RoomModel> getRoomsByCursor(@RequestParam(name = "cursor") String cursor) {
    CursorPage<RoomModel> responseBody = roomService.getRooms(cursor);
    logRoomsApiGet(responseBody);
    return responseBody;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createRoom(@RequestBody @Valid RoomCreateModel requestBody) {
    roomService.requestRoomCreation(requestBody);
    log.info("Created room on Rooms API POST, room={}", requestBody.getUid());
  }

  @GetMapping(path = "/{room}", produces = MediaType.APPLICATION_JSON_VALUE)
  public RoomModel getRoom(@PathVariable("room") String room) {
    RoomModel responseBody = roomService.getRoom(room);
    log.info("Retrieved 1 room on Rooms API GET, room={}", responseBody.getUid());
    return responseBody;
  }

  @PutMapping(
      path = "/{room}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RoomModel updateRoom(
      @PathVariable("room") String room, @RequestBody @Valid RoomUpdateModel requestBody) {
    RoomModel responseBody = roomService.updateRoom(room, requestBody);
    log.info("Updated room on Rooms API PUT, room={}", responseBody.getUid());
    return responseBody;
  }

  @DeleteMapping(path = "/{room}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRoom(@PathVariable("room") String room) {
    roomService.requestRoomDeletion(room);
    log.info("Deleted room on Rooms API DELETE, room={}", room);
  }
}
