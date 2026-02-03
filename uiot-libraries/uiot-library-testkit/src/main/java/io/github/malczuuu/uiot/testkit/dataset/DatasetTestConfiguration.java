package io.github.malczuuu.uiot.testkit.dataset;

import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class DatasetTestConfiguration {

  @Bean
  public DatasetInitializer datasetInitializer(List<DatasetRefresher> datasetRefreshers) {
    return new DatasetInitializer(datasetRefreshers);
  }
}
