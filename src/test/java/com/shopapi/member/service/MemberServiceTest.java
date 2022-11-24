package com.shopapi.member.service;

import com.shopapi.member.domain.Member;
import com.shopapi.member.repository.MemberRepository;
import com.shopapi.member.request.MemberRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}