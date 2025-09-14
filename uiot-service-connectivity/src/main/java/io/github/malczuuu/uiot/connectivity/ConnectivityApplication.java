package io.github.malczuuu.uiot.connectivity;

import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConnectivityApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConnectivityApplication.class, args);
  }

  /**
   * It was observed, that the order of paths in OpenAPI documentation is not deterministic. This
   * customizer sorts the paths alphabetically to ensure a consistent generated docs order.
   */
  @Bean
  public OpenApiCustomizer openApiPathSorter() {
    return openApi -> {
      String version = getClass().getPackage().getImplementationVersion();
      openApi.setInfo(
          new Info()
              .version(version != null ? version : "compiled")
              .title("uIoT Connectivity Service API"));

      Paths paths = new Paths();

      openApi.getPaths().keySet().stream()
          .sorted()
          .forEach(path -> paths.addPathItem(path, openApi.getPaths().get(path)));

      openApi.setPaths(paths);
    };
  }
}
