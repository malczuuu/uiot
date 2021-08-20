package io.github.malczuuu.uiot.connectivity.rest;

import io.github.malczuuu.uiot.connectivity.core.ConnectivityService;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityCreateModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityUpdateModel;
import io.github.malczuuu.uiot.connectivity.model.PasswordUpdateModel;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/rooms/{room}/things/{thing}/connectivity")
public class ConnectivityController {

  private final ConnectivityService connectivityService;

  public ConnectivityController(ConnectivityService connectivityService) {
    this.connectivityService = connectivityService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ConnectivityModel getConnectivity(
      @PathVariable("room") String room, @PathVariable("thing") String thing) {
    return connectivityService.getConnectivity(room, thing);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ConnectivityModel createConnectivity(
      @PathVariable("room") String room,
      @PathVariable("thing") String thing,
      @RequestBody @Valid ConnectivityCreateModel requestBody) {
    return connectivityService.createConnectivity(room, thing, requestBody);
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ConnectivityModel updateConnectivity(
      @PathVariable("room") String room,
      @PathVariable("thing") String thing,
      @RequestBody @Valid ConnectivityUpdateModel requestBody) {
    return connectivityService.updateConnectivity(room, thing, requestBody);
  }

  @PutMapping(path = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePassword(
      @PathVariable("room") String room,
      @PathVariable("thing") String thing,
      @RequestBody @Valid PasswordUpdateModel requestBody) {
    connectivityService.updatePassword(room, thing, requestBody);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteConnectivity(
      @PathVariable("room") String room, @PathVariable("thing") String thing) {
    connectivityService.deleteConnectivity(room, thing);
  }
}
