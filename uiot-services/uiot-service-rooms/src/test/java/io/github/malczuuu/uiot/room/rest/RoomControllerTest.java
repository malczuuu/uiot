package io.github.malczuuu.uiot.room.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import io.github.malczuuu.uiot.room.core.RoomEntity;
import io.github.malczuuu.uiot.room.core.RoomRepository;
import io.github.malczuuu.uiot.room.model.CursorPage;
import io.github.malczuuu.uiot.room.model.RoomCreateModel;
import io.github.malczuuu.uiot.room.model.RoomModel;
import io.github.malczuuu.uiot.room.model.RoomUpdateModel;
import io.github.malczuuu.uiot.room.testkit.RoomServiceTest;
import io.github.malczuuu.uiot.testkit.RefreshDatasetAfter;
import io.github.problem4j.core.Problem;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.ExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@RoomServiceTest
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class RoomControllerTest {

  @Autowired private RestTestClient restClient;

  @Autowired private RoomRepository roomRepository;

  @Autowired private JsonMapper jsonMapper;

  private List<RoomEntity> rooms;

  @BeforeEach
  void beforeEach() {
    rooms = roomRepository.findAll();
  }

  @ParameterizedTest
  @CsvSource({"40, 35, false", "35, 35, true", "30, 30, true", "1, 1, true"})
  void givenRoomsInDatabase_whenGetRooms_thenRetrievesFirstPage(
      int requestedSize, int expectedCount, boolean shouldHaveNext) {
    ExchangeResult result =
        restClient.get().uri("/api/rooms?size=" + requestedSize).exchange().returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);

    CursorPage<RoomModel> page =
        jsonMapper.readValue(result.getResponseBodyContent(), new TypeReference<>() {});

    List<String> expectedUids =
        rooms.stream()
            .sorted(Comparator.comparing(RoomEntity::getId))
            .limit(expectedCount)
            .map(RoomEntity::getUid)
            .toList();

    assertThat(page.getContent()).hasSize(expectedCount);
    assertThat(page.getContent())
        .extracting(RoomModel::getUid)
        .containsExactlyElementsOf(expectedUids);

    if (shouldHaveNext) {
      assertThat(page.getLinks().getNextPath()).isNotNull();
    } else {
      assertThat(page.getLinks()).isNull();
    }
  }

  @Test
  void givenRoomsInDatabase_whenGetRoomsByCursor_thenRetrievesNextPage() {
    ExchangeResult firstResult =
        restClient.get().uri("/api/rooms?size=10").exchange().returnResult();

    assertThat(firstResult.getStatus()).isEqualTo(HttpStatus.OK);

    CursorPage<RoomModel> firstPage =
        jsonMapper.readValue(firstResult.getResponseBodyContent(), new TypeReference<>() {});

    assertThat(firstPage.getContent()).hasSize(10);
    assertThat(firstPage.getLinks().getNextPath()).isNotNull();

    String nextPath = firstPage.getLinks().getNextPath();
    ExchangeResult secondResult = restClient.get().uri(nextPath).exchange().returnResult();

    assertThat(secondResult.getStatus()).isEqualTo(HttpStatus.OK);

    CursorPage<RoomModel> secondPage =
        jsonMapper.readValue(secondResult.getResponseBodyContent(), new TypeReference<>() {});

    assertThat(secondPage.getContent()).hasSize(10);
    assertThat(secondPage.getContent())
        .extracting(RoomModel::getUid)
        .doesNotContainAnyElementsOf(
            firstPage.getContent().stream().map(RoomModel::getUid).toList());
  }

  @Test
  @RefreshDatasetAfter
  void givenRoomCreateModel_whenCreateRoom_thenRoomIsCreatedAsynchronously() {
    String uid = "created-room-uid";

    ExchangeResult result =
        restClient
            .post()
            .uri("/api/rooms")
            .body(new RoomCreateModel(uid, "room name"))
            .exchange()
            .returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.ACCEPTED);

    Optional<RoomEntity> optionalRoom =
        await()
            .atMost(Duration.ofSeconds(3))
            .with()
            .pollInterval(Duration.ofMillis(100))
            .until(() -> roomRepository.findByUid(uid), Optional::isPresent);

    assertThat(optionalRoom).isPresent();
    assertThat(optionalRoom)
        .hasValueSatisfying(
            room -> {
              assertThat(room.getId()).isNotNull();
              assertThat(room.getUid()).isEqualTo(uid);
              assertThat(room.getName()).isEqualTo("room name");
              assertThat(room.getVersion()).isEqualTo(0L);
            });
  }

  @Test
  void givenExistingRoom_whenGetRoom_thenRetrievesRoomModel() {
    RoomEntity existingRoom = rooms.getFirst();

    ExchangeResult result =
        restClient.get().uri("/api/rooms/{uid}", existingRoom.getUid()).exchange().returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);

    RoomModel roomModel =
        jsonMapper.readValue(result.getResponseBodyContent(), new TypeReference<>() {});

    assertThat(roomModel.getUid()).isEqualTo(existingRoom.getUid());
    assertThat(roomModel.getName()).isEqualTo(existingRoom.getName());
    assertThat(roomModel.getVersion()).isEqualTo(existingRoom.getVersion());
  }

  @Test
  void givenNonExistingRoom_whenGetRoom_thenReturnsNotFound() {
    ExchangeResult result =
        restClient.get().uri("/api/rooms/non-existing-uid").exchange().returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

    Problem problem = jsonMapper.readValue(result.getResponseBodyContent(), Problem.class);

    assertThat(problem.getTitle()).isEqualTo("Not Found");
    assertThat(problem.getStatus()).isEqualTo(404);
    assertThat(problem.getDetail()).isEqualTo("room not found");
  }

  @Test
  @RefreshDatasetAfter
  void givenExistingRoomAndUpdateModel_whenUpdateRoom_thenRoomIsUpdated() {
    RoomEntity existingRoom = rooms.getFirst();

    ExchangeResult result =
        restClient
            .put()
            .uri("/api/rooms/{uid}", existingRoom.getUid())
            .body(new RoomUpdateModel("updated name", existingRoom.getVersion()))
            .exchange()
            .returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);

    RoomModel roomModel =
        jsonMapper.readValue(result.getResponseBodyContent(), new TypeReference<>() {});

    assertThat(roomModel.getUid()).isEqualTo(existingRoom.getUid());
    assertThat(roomModel.getName()).isEqualTo("updated name");
    assertThat(roomModel.getVersion()).isEqualTo(existingRoom.getVersion() + 1);
  }

  @Test
  void givenNonExistingRoom_whenUpdateRoom_thenReturnsNotFound() {
    ExchangeResult result =
        restClient
            .put()
            .uri("/api/rooms/non-existing-uid")
            .body(new RoomUpdateModel("updated name", 0L))
            .exchange()
            .returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

    Problem problem = jsonMapper.readValue(result.getResponseBodyContent(), Problem.class);

    assertThat(problem.getTitle()).isEqualTo("Not Found");
    assertThat(problem.getStatus()).isEqualTo(404);
    assertThat(problem.getDetail()).isEqualTo("room not found");
  }

  @Test
  @RefreshDatasetAfter
  void givenExistingRoom_whenDeleteRoom_thenRoomIsDeletedAsynchronously() {
    RoomEntity existingRoom = rooms.getFirst();

    ExchangeResult result =
        restClient
            .delete()
            .uri("/api/rooms/{uid}", existingRoom.getUid())
            .exchange()
            .returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);

    await()
        .atMost(Duration.ofSeconds(3))
        .with()
        .pollInterval(Duration.ofMillis(100))
        .until(() -> !roomRepository.existsByUid(existingRoom.getUid()));
  }

  @Test
  void givenNonExistingRoom_whenDeleteRoom_thenNoErrorIsReturned() {
    ExchangeResult result =
        restClient.delete().uri("/api/rooms/non-existing-uid").exchange().returnResult();

    assertThat(result.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
