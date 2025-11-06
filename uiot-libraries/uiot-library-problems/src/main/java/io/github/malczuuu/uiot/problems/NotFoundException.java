package io.github.malczuuu.uiot.problems;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemException;

public class NotFoundException extends ProblemException {

  public NotFoundException(String detail) {
    super(
        Problem.builder()
            .type(Problem.BLANK_TYPE)
            .title("Not Found")
            .status(404)
            .detail(detail)
            .build());
  }
}
