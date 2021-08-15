package io.github.malczuuu.uiot.connectivity.core;

import io.github.malczuuu.uiot.connectivity.model.ConnectivityCreateModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityUpdateModel;
import io.github.malczuuu.uiot.connectivity.model.PasswordUpdateModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ConnectivityService {

  ConnectivityModel getConnectivity(String room, String thing);

  ConnectivityModel createConnectivity(
      String room, String thing, ConnectivityCreateModel connectivity);

  ConnectivityModel updateConnectivity(
      String room, String thing, ConnectivityUpdateModel connectivity);

  void updatePassword(String room, String thing, PasswordUpdateModel password);

  void deleteConnectivity(String room, String thing);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  class ConnectivityNotFoundException extends RuntimeException {}

  @ResponseStatus(HttpStatus.CONFLICT)
  class ConcurrentUpdateException extends RuntimeException {}
}
