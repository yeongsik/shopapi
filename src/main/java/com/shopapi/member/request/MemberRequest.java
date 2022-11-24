package com.shopapi.member.request;

import com.shopapi.member.domain.Address;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Builder
public class MemberRequest {

    @NotBlank(message = "잘못된 이름 값입니다.")
    private String name;
    @NotBlank(message = "잘못된 패스워드 값입니다.")
    private String password;
    @NotBlank(message = "잘못된 이메일 값입니다.")
    private String email;

    private List<Address> addressList;

}
