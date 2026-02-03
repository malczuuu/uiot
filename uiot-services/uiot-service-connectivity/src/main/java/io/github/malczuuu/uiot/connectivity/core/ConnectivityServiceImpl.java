package io.github.malczuuu.uiot.connectivity.core;

import io.github.malczuuu.uiot.connectivity.model.ConnectivityCreateModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityUpdateModel;
import io.github.malczuuu.uiot.connectivity.model.PasswordUpdateModel;
import io.github.malczuuu.uiot.model.error.ConcurrentUpdateException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class ConnectivityServiceImpl implements ConnectivityService {

  private final ConnectivityRepository connectivityRepository;

  public ConnectivityServiceImpl(ConnectivityRepository connectivityRepository) {
    this.connectivityRepository = connectivityRepository;
  }

  @Override
  public ConnectivityModel getConnectivity(String room, String thing) {
    ConnectivityEntity entity =
        connectivityRepository
            .findByRoomUidAndThingUid(room, thing)
            .orElseThrow(ConnectivityNotFoundException::new);
    return mapToDto(entity);
  }

  @Override
  public ConnectivityModel createConnectivity(
      String room, String thing, ConnectivityCreateModel connectivity) {
    ConnectivityEntity entity =
        new ConnectivityEntity(room, thing, connectivity.getPassword(), connectivity.getEnabled());
    entity = connectivityRepository.save(entity);
    return mapToDto(entity);
  }

  @Override
  public ConnectivityModel updateConnectivity(
      String room, String thing, ConnectivityUpdateModel connectivity) {
    ConnectivityEntity entity =
        connectivityRepository
            .findByRoomUidAndThingUid(room, thing)
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

  @Override
  public void updatePassword(String room, String thing, PasswordUpdateModel password) {
    ConnectivityEntity entity =
        connectivityRepository
            .findByRoomUidAndThingUid(room, thing)
            .orElseThrow(ConnectivityNotFoundException::new);

    entity.setPassword(password.getPassword());
    entity.setVersion(password.getVersion());
    try {
      connectivityRepository.save(entity);
    } catch (OptimisticLockingFailureException e) {
      throw new ConcurrentUpdateException();
    }
  }

  @Override
  public void deleteConnectivity(String room, String thing) {
    connectivityRepository.deleteByRoomUidAndThingUid(room, thing);
  }

  private ConnectivityModel mapToDto(ConnectivityEntity entity) {
    return new ConnectivityModel(
        entity.getRoomUid(), entity.getThingUid(), entity.isEnabled(), entity.getVersion());
  }

  @Override
  public void deleteConnectivity(String roomUid) {
    connectivityRepository.deleteByRoomUid(roomUid);
  }
}
