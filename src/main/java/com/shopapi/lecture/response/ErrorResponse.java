package com.shopapi.lecture.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "validation": {
 *         "title" : "값을 입력해주세요"
 *     }
 * }
 */
@Getter
public class ErrorResponse {
    // 회사마다 , 팀마다 에러 클래스를 정의하는 것은 다르다.

    // 개선해볼 점 : validation을 맵이 아닌 클래스로 변경하기
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();
    @Builder
    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }

}
