package io.github.malczuuu.uiot.model.error;

import io.github.problem4j.core.Problem;
import io.github.problem4j.core.ProblemException;

public class BadRequestException extends ProblemException {

  public BadRequestException(String detail) {
    super(
        Problem.builder()
            .type(Problem.BLANK_TYPE)
            .title("Bad Request")
            .status(400)
            .detail(detail)
            .build());
  }
}
