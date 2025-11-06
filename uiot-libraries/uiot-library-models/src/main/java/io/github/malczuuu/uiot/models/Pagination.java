package io.github.malczuuu.uiot.models;

public class Pagination {

  public static final int DEFAULT_SIZE = 20;

  public static Pagination parseSize(String size) {
    return parseSize(size, DEFAULT_SIZE);
  }

  public static Pagination parseSize(String size, int defaultValue) {
    int sizeAsInt;
    try {
      sizeAsInt = Integer.parseInt(size);
    } catch (NumberFormatException e) {
      sizeAsInt = defaultValue;
    }
    return new Pagination(sizeAsInt);
  }

  private final int size;

  public Pagination(int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }
}
