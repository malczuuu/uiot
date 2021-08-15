package io.github.malczuuu.uiot.connectivity.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.malczuuu.uiot.connectivity.model.ConnectivityCreateModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityModel;
import io.github.malczuuu.uiot.connectivity.model.ConnectivityUpdateModel;
import io.github.malczuuu.uiot.connectivity.model.PasswordUpdateModel;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.dao.OptimisticLockingFailureException;

class ConnectivityServiceTests {

  private ConnectivityRepository connectivityRepository;

  private ConnectivityService connectivityService;

  @BeforeEach
  void beforeEach() {
    connectivityRepository = mock(ConnectivityRepository.class);
    connectivityService = new ConnectivityServiceImpl(connectivityRepository);
  }

  @Test
  void shouldReturnConnectivity() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(roomUid, thingUid))
        .thenReturn(
            Optional.of(
                new ConnectivityEntity(new ObjectId(), roomUid, thingUid, "password", true)));

    ConnectivityModel connectivity = connectivityService.getConnectivity(roomUid, thingUid);

    assertEquals(roomUid, connectivity.getRoom());
    assertEquals(thingUid, connectivity.getThing());
    assertTrue(connectivity.isEnabled());
  }

  @Test
  void shouldThrowConnectivityNotFound() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(anyString(), anyString()))
        .thenReturn(Optional.empty());

    assertThrows(
        ConnectivityNotFoundException.class,
        () -> connectivityService.getConnectivity(roomUid, thingUid));
  }

  @Test
  void shouldCreateConnectivity() {
    String roomUid = "room";
    String thingUid = "thing";
    doAnswer(this::mockConnectivityInsert)
        .when(connectivityRepository)
        .save(any(ConnectivityEntity.class));

    ConnectivityModel connectivity =
        connectivityService.createConnectivity(
            roomUid, thingUid, new ConnectivityCreateModel("password", false));

    assertEquals(roomUid, connectivity.getRoom());
    assertEquals(thingUid, connectivity.getThing());
    assertFalse(connectivity.isEnabled());
    assertEquals(0L, connectivity.getVersion());
  }

  private ConnectivityEntity mockConnectivityInsert(InvocationOnMock invocation) {
    ConnectivityEntity entity = invocation.getArgument(0, ConnectivityEntity.class);
    return new ConnectivityEntity(
        new ObjectId(),
        entity.getRoomUid(),
        entity.getThingUid(),
        entity.getPassword(),
        entity.isEnabled(),
        0L);
  }

  @Test
  void shouldUpdateConnectivity() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(roomUid, thingUid))
        .thenReturn(
            Optional.of(
                new ConnectivityEntity(new ObjectId(), roomUid, thingUid, "password", true, 0L)));
    doAnswer(i -> mockConnectivityUpdate(i, new Holder<>()))
        .when(connectivityRepository)
        .save(any(ConnectivityEntity.class));

    ConnectivityModel connectivity =
        connectivityService.updateConnectivity(
            roomUid, thingUid, new ConnectivityUpdateModel(false, 0L));

    assertFalse(connectivity.isEnabled());
    assertEquals(1L, connectivity.getVersion());
  }

  private ConnectivityEntity mockConnectivityUpdate(
      InvocationOnMock invocation, Holder<ConnectivityEntity> call) {
    ConnectivityEntity entity = invocation.getArgument(0, ConnectivityEntity.class);
    call.value = entity;
    return new ConnectivityEntity(
        entity.getId(),
        entity.getRoomUid(),
        entity.getThingUid(),
        entity.getPassword(),
        entity.isEnabled(),
        entity.getVersion() + 1L);
  }

  @Test
  void shouldThrowNotFoundOnAttemptToUpdateConnectivity() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(anyString(), anyString()))
        .thenReturn(Optional.empty());

    assertThrows(
        ConnectivityNotFoundException.class,
        () ->
            connectivityService.updateConnectivity(
                roomUid, thingUid, new ConnectivityUpdateModel(false, 0L)));
  }

  @Test
  void shouldThrowConcurrentModificationOnAttemptToUpdateConnectivity() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(roomUid, thingUid))
        .thenReturn(
            Optional.of(
                new ConnectivityEntity(new ObjectId(), roomUid, thingUid, "password", true, 0L)));
    when(connectivityRepository.save(any(ConnectivityEntity.class)))
        .thenThrow(
            new OptimisticLockingFailureException("Optimistic lock exception on saving entity"));

    assertThrows(
        ConcurrentUpdateException.class,
        () ->
            connectivityService.updateConnectivity(
                roomUid, thingUid, new ConnectivityUpdateModel(false, 0L)));
  }

  @Test
  void shouldUpdateConnectivityPassword() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(roomUid, thingUid))
        .thenReturn(
            Optional.of(
                new ConnectivityEntity(new ObjectId(), roomUid, thingUid, "password1", true, 0L)));
    Holder<ConnectivityEntity> call = new Holder<>();
    doAnswer(i -> mockConnectivityUpdate(i, call))
        .when(connectivityRepository)
        .save(any(ConnectivityEntity.class));

    connectivityService.updatePassword(roomUid, thingUid, new PasswordUpdateModel("password2", 0L));

    assertEquals("password2", call.value.getPassword());
    assertEquals(0L, call.value.getVersion());
  }

  @Test
  void shouldThrowNotFoundOnAttemptToUpdateConnectivityPassword() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(anyString(), anyString()))
        .thenReturn(Optional.empty());

    assertThrows(
        ConnectivityNotFoundException.class,
        () ->
            connectivityService.updatePassword(
                roomUid, thingUid, new PasswordUpdateModel("password2", 0L)));
  }

  @Test
  void shouldThrowConcurrentModificationOnAttemptToUpdateConnectivityPassword() {
    String roomUid = "room";
    String thingUid = "thing";
    when(connectivityRepository.findByRoomUidAndThingUid(roomUid, thingUid))
        .thenReturn(
            Optional.of(
                new ConnectivityEntity(new ObjectId(), roomUid, thingUid, "password", true, 0L)));
    when(connectivityRepository.save(any(ConnectivityEntity.class)))
        .thenThrow(
            new OptimisticLockingFailureException("Optimistic lock exception on saving entity"));

    assertThrows(
        ConcurrentUpdateException.class,
        () ->
            connectivityService.updatePassword(
                roomUid, thingUid, new PasswordUpdateModel("password2", 0L)));
  }

  @Test
  void shouldDeleteConnectivity() {
    String roomUid = "room";
    String thingUid = "thing";
    Holder<Boolean> call = new Holder<>(false);
    doAnswer(i -> mockConnectivityDelete(i, call))
        .when(connectivityRepository)
        .deleteByRoomUidAndThingUid(roomUid, thingUid);

    connectivityService.deleteConnectivity(roomUid, thingUid);

    assertTrue(call.value);
  }

  private Void mockConnectivityDelete(InvocationOnMock invocation, Holder<Boolean> call) {
    call.value = true;
    return null;
  }

  private static class Holder<T> {

    private T value;

    private Holder() {
      this(null);
    }

    private Holder(T value) {
      this.value = value;
    }
  }
}
