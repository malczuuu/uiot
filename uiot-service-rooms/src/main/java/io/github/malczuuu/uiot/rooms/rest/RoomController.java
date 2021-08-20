package io.github.malczuuu.uiot.rooms.rest;

import io.github.malczuuu.uiot.rooms.core.RoomService;
import io.github.malczuuu.uiot.rooms.model.CursorPage;
import io.github.malczuuu.uiot.rooms.model.RoomCreateModel;
import io.github.malczuuu.uiot.rooms.model.RoomModel;
import io.github.malczuuu.uiot.rooms.model.RoomUpdateModel;
import javax.validation.Valid;
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

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public CursorPage<RoomModel> getRooms(
      @RequestParam(name = "size", defaultValue = "20") String size) {
    int sizeAsInt = parseSize(size);
    return roomService.getRooms(sizeAsInt);
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

  @ApiIgnore
  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      params = {"cursor"})
  public CursorPage<RoomModel> getRoomsByCursor(@RequestParam(name = "cursor") String cursor) {
    return roomService.getRooms(cursor);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void createRoom(@RequestBody @Valid RoomCreateModel requestBody) {
    roomService.requestRoomCreation(requestBody);
  }

  @GetMapping(path = "/{room}", produces = MediaType.APPLICATION_JSON_VALUE)
  public RoomModel getRoom(@PathVariable("room") String room) {
    return roomService.getRoom(room);
  }

  @PutMapping(
      path = "/{room}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RoomModel updateRoom(
      @PathVariable("room") String room, @RequestBody @Valid RoomUpdateModel requestBody) {
    return roomService.updateRoom(room, requestBody);
  }

  @DeleteMapping(path = "/{room}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRoom(@PathVariable("room") String room) {
    roomService.requestRoomDeletion(room);
  }
}
