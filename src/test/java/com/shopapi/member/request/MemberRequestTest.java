package com.shopapi.member.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class MemberRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("MemberRequest 데이터 검증 테스트")
    void notBlankTest() {
        /*
            구글링 중 Request 클래스까지 테스트하는 예제가 있어서 따라 작성
         */
        MemberRequest memberRequest = MemberRequest.builder()
                .name("")
                .password("1234")
                .email("1234@naver.com")
                .build();

        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        for (ConstraintViolation<MemberRequest> violation : violations) {
            System.err.println(violation.getMessage());
        }
    }
}