package io.github.malczuuu.uiot.history;

import io.github.malczuuu.problem4j.spring.web.EnableProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableProblem
@SpringBootApplication
@ConfigurationPropertiesScan
public class HistoryApplication {

  public static void main(String[] args) {
    SpringApplication.run(HistoryApplication.class, args);
  }
}
