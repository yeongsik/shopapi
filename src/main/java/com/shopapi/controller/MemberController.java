package com.shopapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/api/members")
    public String getMembers() {
        return "Members";
    }

    @GetMapping("/api/members/{memberId}")
    public String findMember(String memberId) {
        return memberId;
    }
}
