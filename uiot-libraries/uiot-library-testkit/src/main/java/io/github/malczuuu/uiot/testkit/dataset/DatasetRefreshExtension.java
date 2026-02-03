package io.github.malczuuu.uiot.testkit.dataset;

import io.github.malczuuu.uiot.testkit.CommonTestTag;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatasetRefreshExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) {
    if (!context.getTags().contains(CommonTestTag.REFRESH_DATASET_AFTER)) {
      return;
    }

    SpringExtension.getApplicationContext(context)
        .getBeansOfType(DatasetRefresher.class)
        .values()
        .forEach(DatasetRefresher::refresh);
  }
}
