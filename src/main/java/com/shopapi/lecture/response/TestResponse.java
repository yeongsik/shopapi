package com.shopapi.lecture.response;

import com.shopapi.lecture.domain.TestEntity;
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

    // 생성자 오버로딩
    public TestResponse(TestEntity test) {
        this.id = test.getId();
        this.title = test.getTitle();
        this.content = test.getContent();
    }

    @Builder
    public TestResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
