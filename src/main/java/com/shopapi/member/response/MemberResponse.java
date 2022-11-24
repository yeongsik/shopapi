package com.shopapi.member.response;

import com.shopapi.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

    private Long id;
    private String name;
    private String email;
    private String password;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
    }
}
