package com.shopapi.lecture.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TestEditor {
    // 수정할 데이터만 필드로 만들어준다.

    private final String title;
    private final String content;

    @Builder
    public TestEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
