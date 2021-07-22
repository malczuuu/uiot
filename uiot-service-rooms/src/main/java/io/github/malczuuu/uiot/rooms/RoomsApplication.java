package io.github.malczuuu.uiot.rooms;

import io.github.malczuuu.problem4j.spring.web.EnableProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProblem
@SpringBootApplication
public class RoomsApplication {

  public static void main(String[] args) {
    SpringApplication.run(RoomsApplication.class, args);
  }
}
