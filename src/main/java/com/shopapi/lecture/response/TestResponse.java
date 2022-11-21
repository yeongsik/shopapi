package com.shopapi.lecture.response;

import lombok.Builder;
import lombok.Getter;

/**
 *  서비스 정책에 맞는 클래스
 */
@Getter
public class TestResponse {

    private Long id;
    private String title;
    private String content;

    @Builder
    public TestResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
