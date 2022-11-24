package com.shopapi.member.controller;

import com.shopapi.member.request.MemberRequest;
import com.shopapi.member.response.MemberResponse;
import com.shopapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public void signUpMember(@RequestBody @Valid MemberRequest request) {
        // 회원 저장
        memberService.signUpMember(request);
    }

    // TODO: 2022/11/19 단건 조회
    @GetMapping("/api/members/{memberId}")
    public MemberResponse findMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }

    // TODO: 2022/11/19 다수 조회
    @GetMapping("/api/members")
    public List<MemberResponse> getMemberList(Pageable page) {
        return memberService.getMemberList(page);
    }

    // TODO: 2022/11/19 회원 정보 수정

    // TODO: 2022/11/19 회원 삭제

}

