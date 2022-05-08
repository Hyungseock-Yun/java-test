package com.example.javatest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {   // JUnit5에서는 public을 선언하지 않아도 된다.

  @Test
  void creat() {
    Study study = new Study();
    assertNotNull(study);
    System.out.println("create");
  }

  @Test
  @Disabled
  void creat1() {
    Study study = new Study();
    assertNotNull(study);
    System.out.println("create1");
  }

  @BeforeAll
  static void beforeAll() {
    System.out.println("before all");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("after all");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("before each");
  }

  @AfterEach
  void afterEach() {
    System.out.println("after each");
  }

}