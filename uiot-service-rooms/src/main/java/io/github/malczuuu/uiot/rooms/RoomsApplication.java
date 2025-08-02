package io.github.malczuuu.uiot.rooms;

import io.github.malczuuu.problem4j.spring.web.EnableProblem;
import java.time.Clock;
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
public class RoomsApplication {

  public static void main(String[] args) {
    SpringApplication.run(RoomsApplication.class, args);
  }

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public Docket docket() {
    String version = RoomsApplication.class.getPackage().getImplementationVersion();

    ApiInfo apiInfo =
        new ApiInfo(
            "uIoT Rooms API",
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
        .paths(path -> path.startsWith("/api"))
        .build();
  }
}
