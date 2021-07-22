package io.github.malczuuu.uiot.things;

import io.github.malczuuu.problem4j.spring.web.EnableProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProblem
@SpringBootApplication
public class ThingsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThingsApplication.class, args);
  }
}
