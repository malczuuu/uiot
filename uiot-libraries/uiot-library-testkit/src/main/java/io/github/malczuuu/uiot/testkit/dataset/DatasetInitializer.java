package io.github.malczuuu.uiot.testkit.dataset;

import java.util.List;
import org.springframework.beans.factory.SmartInitializingSingleton;

public class DatasetInitializer implements SmartInitializingSingleton {

  private final List<DatasetRefresher> datasetRefreshers;

  public DatasetInitializer(List<DatasetRefresher> datasetRefreshers) {
    this.datasetRefreshers = datasetRefreshers;
  }

  @Override
  public void afterSingletonsInstantiated() {
    datasetRefreshers.forEach(DatasetRefresher::refresh);
  }
}
