package io.github.malczuuu.uiot.connectivity.core;

import io.github.malczuuu.uiot.connectivity.model.ConnectivityCreateModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityUpdateModel;
import io.github.malczuuu.uiot.connectivity.model.PasswordUpdateModel;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class ConnectivityService {

  private final ConnectivityRepository connectivityRepository;

  public ConnectivityService(ConnectivityRepository connectivityRepository) {
    this.connectivityRepository = connectivityRepository;
  }

  public ConnectivityModel getConnectivity(String room, String thing) {
    ConnectivityEntity entity =
        connectivityRepository
            .findByRoomAndThing(room, thing)
            .orElseThrow(ConnectivityNotFoundException::new);
    return mapToDto(entity);
  }

  public ConnectivityModel createConnectivity(
      String room, String thing, ConnectivityCreateModel connectivity) {
    ConnectivityEntity entity =
        new ConnectivityEntity(room, thing, connectivity.getPassword(), connectivity.getEnabled());
    entity = connectivityRepository.save(entity);
    return mapToDto(entity);
  }

  public ConnectivityModel updateConnectivity(
      String room, String thing, ConnectivityUpdateModel connectivity) {
    ConnectivityEntity entity =
        connectivityRepository
            .findByRoomAndThing(room, thing)
            .orElseThrow(ConnectivityNotFoundException::new);

    entity.setEnabled(connectivity.getEnabled());
    entity.setVersion(connectivity.getVersion());
    try {
      entity = connectivityRepository.save(entity);
    } catch (OptimisticLockingFailureException e) {
      throw new ConcurrentUpdateException();
    }

    return mapToDto(entity);
  }

  public void updatePassword(String room, String thing, PasswordUpdateModel password) {
    ConnectivityEntity entity =
        connectivityRepository
            .findByRoomAndThing(room, thing)
            .orElseThrow(ConnectivityNotFoundException::new);

    entity.setPassword(password.getPassword());
    entity.setVersion(password.getVersion());
    try {
      connectivityRepository.save(entity);
    } catch (OptimisticLockingFailureException e) {
      throw new ConcurrentUpdateException();
    }
  }

  public void deleteConnectivity(String room, String thing) {
    connectivityRepository.deleteByRoomAndThing(room, thing);
  }

  private ConnectivityModel mapToDto(ConnectivityEntity entity) {
    return new ConnectivityModel(
        entity.getRoom(), entity.getThing(), entity.isEnabled(), entity.getVersion());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  public static class ConnectivityNotFoundException extends RuntimeException {}

  @ResponseStatus(HttpStatus.CONFLICT)
  public static class ConcurrentUpdateException extends RuntimeException {}
}
