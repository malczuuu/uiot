package io.github.malczuuu.uiot.rules;

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
public class RulesApplication {

  public static void main(String[] args) {
    SpringApplication.run(RulesApplication.class, args);
  }

  @Bean
  public Docket docket() {
    ApiInfo apiInfo =
        new ApiInfo(
            "uIoT Rules API",
            "",
            "1.0.0-SNAPSHOT",
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
