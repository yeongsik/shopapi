package com.shopapi.member.service;

import com.shopapi.member.domain.Member;
import com.shopapi.member.repository.MemberRepository;
import com.shopapi.member.request.MemberRequest;
import com.shopapi.member.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    // 회원 다수 조회
    public List<MemberResponse> getMemberList(Pageable page) {
        // Java 8 스트링 문법 익히기
        return memberRepository.findAll(page).stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    // 회원 단건 조회
    public MemberResponse getMember(Long memberId) {
        // Java 8 Optional 문법 익히기
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return new MemberResponse(findMember);

    }
}
