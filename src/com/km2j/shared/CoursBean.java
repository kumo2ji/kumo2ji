package com.km2j.shared;

import java.io.Serializable;

public class CoursBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private int year;
  private int cours;

  public final int getYear() {
    return year;
  }

  public final void setYear(final int year) {
    this.year = year;
  }

  public final int getCours() {
    return cours;
  }

  public final void setCours(final int cours) {
    this.cours = cours;
  }

}
