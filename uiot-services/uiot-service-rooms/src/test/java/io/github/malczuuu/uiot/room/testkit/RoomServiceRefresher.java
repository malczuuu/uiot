package io.github.malczuuu.uiot.room.testkit;

import io.github.malczuuu.uiot.room.core.RoomEntity;
import io.github.malczuuu.uiot.room.core.RoomRepository;
import io.github.malczuuu.uiot.testkit.dataset.DatasetRefresher;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomServiceRefresher implements DatasetRefresher {

  private static final Logger log = LoggerFactory.getLogger(RoomServiceRefresher.class);

  private final RoomRepository roomRepository;

  public RoomServiceRefresher(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Override
  public void refresh() {
    roomRepository.deleteAll();

    List<RoomEntity> rooms = new RoomEntityFactory().generateRooms(35);
    roomRepository.saveAll(rooms);

    log.info("Refreshed service dataset");
  }
}
