package com.shopapi.member.service;

import com.shopapi.member.domain.Member;
import com.shopapi.member.repository.MemberRepository;
import com.shopapi.member.request.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 저장
    public void signUpMember(MemberRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        memberRepository.save(member);
    }

    // 회원 단건 조회

    // 회원 다수 조회
}
