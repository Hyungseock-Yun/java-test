package com.example.javatest;

import com.example.javatest.member.MemberService;
import com.example.javatest.study.StudyRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
public class StudyServiceTest {

  @Mock
  MemberService memberService;

  @Autowired
  StudyRepository studyRepository;

  @Container
  private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
    .withDatabaseName("studytest");

// @Container 어노테이션으로 대체 가능
//  @BeforeAll
//  static void beforeAll() {
//    postgreSQLContainer.start();
//    System.out.println(postgreSQLContainer.getJdbcUrl());
//  }
//
//  @AfterAll
//  static void afterAll() {
//    postgreSQLContainer.stop();
//  }

  @BeforeEach
  void beforeEach() {

  }
}
