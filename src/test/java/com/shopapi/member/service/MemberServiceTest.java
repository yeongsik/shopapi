package com.shopapi.member.service;

import com.shopapi.member.domain.Member;
import com.shopapi.member.repository.MemberRepository;
import com.shopapi.member.request.MemberRequest;
import com.shopapi.member.response.MemberResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 저장 테스트")
    void saveMember() {
        //given
        MemberRequest request = MemberRequest.builder()
                .name("이름1")
                .email("1234@naver.com")
                .password("1234")
                .build();

        //when
        memberService.signUpMember(request);

        //then
        Assertions.assertEquals(1L,memberRepository.count());
        Member member = memberRepository.findAll().get(0);
        Assertions.assertEquals("이름1",member.getName());
        Assertions.assertEquals("1234@naver.com",member.getEmail());
        Assertions.assertEquals("1234",member.getPassword());

    }

    @Test
    @DisplayName("회원 단 건 조회")
    void getMemberById() {

        //given
        Member request = Member.builder()
                .name("테스트 이름")
                .email("1234@naver.com")
                .password("1234")
                .build();
        memberRepository.save(request);

        //when
        MemberResponse member = memberService.getMember(request.getId());

        //then
        Assertions.assertNotNull(member);
        Assertions.assertEquals("테스트 이름" , member.getName());
        Assertions.assertEquals("1234@naver.com" , member.getEmail());
        Assertions.assertEquals("1234" , member.getPassword());

    }

    @Test
    @DisplayName("회원 다건 조회 페이징 처리")
    void getMemberListByPaging () throws Exception {

        //given
        List<Member> requestList = IntStream.range(1, 31)
                .mapToObj(i -> Member.builder()
                        .name("테스트 이름 " + i)
                        .email(i + "@naver.com")
                        .password("1234" + i)
                        .build())
                .collect(Collectors.toList());
        memberRepository.saveAll(requestList);

        //when
        PageRequest pageable = PageRequest.of(0, 5);
        List<MemberResponse> memberList = memberService.getMemberList(pageable);

        //then
        Assertions.assertEquals(5,memberList.size());
        Assertions.assertEquals("테스트 이름 1",memberList.get(0).getName());
        Assertions.assertEquals("1@naver.com",memberList.get(0).getEmail());
        Assertions.assertEquals("12341",memberList.get(0).getPassword());
        Assertions.assertEquals("테스트 이름 5",memberList.get(4).getName());
        Assertions.assertEquals("5@naver.com",memberList.get(4).getEmail());
        Assertions.assertEquals("12345",memberList.get(4).getPassword());

    }
}