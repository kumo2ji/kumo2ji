package com.km2j.server.external;

public final class CoursObject {
  private long id;
  private long year;
  private long cours;

  public CoursObject() {
    super();
  }

  public long getId() {
    return id;
  }

  public long getYear() {
    return year;
  }

  public long getCours() {
    return cours;
  }

  @Override
  public String toString() {
    return "CoursObject [id=" + id + ", year=" + year + ", cours=" + cours + "]";
  }
}
