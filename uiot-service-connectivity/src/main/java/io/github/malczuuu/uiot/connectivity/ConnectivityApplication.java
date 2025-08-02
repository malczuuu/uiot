package io.github.malczuuu.uiot.connectivity;

import io.github.malczuuu.problem4j.spring.web.EnableProblem;
import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableProblem
@SpringBootApplication
public class ConnectivityApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConnectivityApplication.class, args);
  }

  @Bean
  public Docket docket() {
    String version = ConnectivityApplication.class.getPackage().getImplementationVersion();

    ApiInfo apiInfo =
        new ApiInfo(
            "uIoT Connectivity API",
            "",
            version,
            "",
            new Contact("", "", ""),
            "",
            "",
            Collections.emptyList());
    return new Docket(DocumentationType.OAS_30)
        .apiInfo(apiInfo)
        .select()
        .paths(path -> path.startsWith("/api") || path.startsWith("/auth"))
        .build();
  }
}
