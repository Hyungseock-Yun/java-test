package com.example.javatest;

import lombok.Data;

@Data
public class Study {

  private StudyStatus status = StudyStatus.DRAFT;

  private int limit;

  private String name;

  public Study(int limit, String name) {
    this.limit = limit;
    this.name = name;
  }

  public Study(int limit) {
    if (limit < 0) throw new IllegalStateException("limit은 0보다 커야 한다");
    this.limit = limit;
  }
}
