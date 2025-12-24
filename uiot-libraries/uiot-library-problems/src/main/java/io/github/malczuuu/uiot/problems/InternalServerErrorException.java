package io.github.malczuuu.uiot.problems;

import io.github.problem4j.core.Problem;
import io.github.problem4j.core.ProblemException;

public class InternalServerErrorException extends ProblemException {

  public InternalServerErrorException() {
    super(
        Problem.builder()
            .type(Problem.BLANK_TYPE)
            .title("Internal Server Error")
            .status(500)
            .build());
  }

  public InternalServerErrorException(String detail) {
    super(
        Problem.builder()
            .type(Problem.BLANK_TYPE)
            .title("Internal Server Error")
            .status(500)
            .detail(detail)
            .build());
  }
}
