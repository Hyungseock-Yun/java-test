package com.example.javatest.study;

import com.example.javatest.StudyStatus;
import com.example.javatest.domain.Member;
import com.example.javatest.domain.Study;
import com.example.javatest.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudyServiceTest {

  @Mock
  MemberService memberService;

  @Mock
  StudyRepository studyRepository;

  @Test
  void createStudyService() {

    StudyService studyService = new StudyService(memberService, studyRepository);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bagzein@gmail.com");

    when(memberService.findById(any())).thenReturn(Optional.of(member));

    Study study = new Study(10, "java");

    Optional<Member> findById = memberService.findById(1L);
    assertEquals("bagzein@gmail.com", findById.get().getEmail());

    doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
    assertThrows(IllegalArgumentException.class, () -> {
      memberService.validate(1L);
    });

    memberService.validate(2L);
//    studyService.createNewStudy(1L, study);

    assertNotNull(studyService);
  }

  @Test
  void createStudyServiceTest() {

    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);;

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bagzein@gmail.com");

    Study study = new Study(10, "테스트");

    when(memberService.findById(any())).thenReturn(Optional.of(member));
    when(studyRepository.save(study));

    studyService.createNewStudy(1L, study);
    assertEquals(member, study.getOwnerId());

    verify(memberService, times(1)).notify(study);
    verify(memberService, times(1)).notify(member);
    verify(memberService, never()).validate(any());   // 호출 여부

    InOrder inOrder = inOrder(memberService);
    inOrder.verify(memberService).notify(study);
    inOrder.verify(memberService).notify(member);
  }

  @DisplayName("BDDMock으로 만들기")
  @Test
  void createStudyServiceByBDDMock() {

    // Given
    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);;

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bagzein@gmail.com");

    Study study = new Study(10, "테스트");

    given(memberService.findById(any())).willReturn(Optional.of(member));
    given(studyRepository.save(study)).willReturn(study);

    // when
    studyService.createNewStudy(1L, study);

    // then
    assertEquals(member, study.getOwnerId());
    then(memberService).should(times(1)).notify(study);
    then(memberService).shouldHaveNoMoreInteractions();
  }

  @Test
  void openStudy() {

    // given
    StudyService studyService = new StudyService(memberService, studyRepository);
    Study study = new Study(10, "더 자바, 테스트");
    assertNull(study.getOpenedDateTime());
    given(studyRepository.save(study)).willReturn(study);

    // when
    studyService.openStudy(study);

    // then
    assertEquals(StudyStatus.OPENED, study.getStatus());
    assertNotNull(study.getOpenedDateTime());
    then(memberService).should().notify(study);
  }
}
