package io.github.malczuuu.uiot.problems;

import io.github.problem4j.core.Problem;
import io.github.problem4j.core.ProblemException;

public class ConcurrentUpdateException extends ProblemException {

  public ConcurrentUpdateException() {
    super(Problem.builder().title("Conflict").status(409).detail("concurrent update").build());
  }
}
