package io.github.malczuuu.uiot.rooms.testkit;

import io.github.malczuuu.uiot.rooms.core.RoomRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class RoomTestConfiguration {

  @Bean
  public RoomServiceRefresher roomDatasetRefresher(RoomRepository roomRepository) {
    return new RoomServiceRefresher(roomRepository);
  }
}
