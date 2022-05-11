package com.example.javatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)   // @BeforeAll, @AfterAll에 static을 붙이지 않아도 된다.
class StudyTest {   // JUnit5에서는 public을 선언하지 않아도 된다.

  int value = 1;    // @TestInstance(TestInstance.Lifecycle.PER_CLASS)를 사용하면 해당 변수를 어느 테스트에서 사용하든 값이 변경되지 않는다.

  @Test
  @DisplayName("스터디 만들기")
  void creat_new_study() {
    Study study = new Study(10);
    assertAll(
      () -> assertNotNull(study),
      () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT여야 한다."),   // lamda식을 쓰면 문자열연산을 성공시에만 하기 때문에 비용을 절감할 수 있음.
      () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다."),
      () -> assertTimeout(Duration.ofMillis(100), () -> {
        new Study(10);
        Thread.sleep(300);
      }),
      () -> assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
        new Study(10);
        Thread.sleep(300);    // TODO ThreadLocal
      })
    );

    System.out.println("create");
  }

  @Test
  @Disabled
  void creat_new_study_again() {
    Study study = new Study(10);
    assertNotNull(study);
    System.out.println("create1");
  }

  @Test
//  @EnabledOnOs({OS.MAC, OS.LINUX})    // OS에 따라 테스트 가능
  @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_11})    // java 버전에 따라서도 테스트 가능
  @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")    // annotation으로도 환경에 따라 테스트 가능
  void test_environment() {
    String testEnv = System.getenv("TEST_ENV");
//    assumeTrue("LOCAL".equalsIgnoreCase(testEnv));

    assumingThat("LOCAL".equalsIgnoreCase(testEnv), () -> {
      System.out.println("local");
    });

    assumingThat("DEV".equalsIgnoreCase(testEnv), () -> {
      System.out.println("DEV");
    });

    assumingThat("PROD".equalsIgnoreCase(testEnv), () -> {
      System.out.println("PROD");
    });

    Study study = new Study(10);
    assertNotNull(study);
    System.out.println("create1");
  }

  @Test
  @Tag("fast")  // Edit Configurations에서 Tags를 fast로 설정하면 fast만 실행된다.
  void test_tag_fast() {
    System.out.println("fast");
  }

  @Test
  @Tag("slow")
  void test_tag_slow() {
    System.out.println("slow");
  }

  @DisplayName("스터디 만들기")
  @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
  void repeatTest(RepetitionInfo repetitionInfo) {
    System.out.println("test " + repetitionInfo.getCurrentRepetition() + "/" +
      repetitionInfo.getTotalRepetitions());
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
//  @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
  @ValueSource(ints = {10, 20, 40})
  void parameterizedTest(@ConvertWith(StudyConverter.class) Study study) {
    System.out.println(study.getLimit());
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, '자바 스터디'", "20, 스프링"})
  void parameterizedTest2(@AggregateWith(StudyAggregator.class) Study study) {
    System.out.println(study);
  }

  static class StudyAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
      return new Study(accessor.getInteger(0), accessor.getString(1));
    }
  }

  static class StudyConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
      assertEquals(Study.class, targetType, "Can only convert to Study");
      return new Study(Integer.parseInt(source.toString()));
    }
  }

  @FastTest
  void test_custom_tag_fast() {
    System.out.println("FastTest");
  }

  @SlowTest
  void test_custom_tag_slow() {
    System.out.println("FastTest");
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