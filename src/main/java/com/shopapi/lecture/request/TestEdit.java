package com.shopapi.lecture.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class TestEdit {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;
    @NotBlank(message = "콘텐츠를 입력하세요.")
    private String content;

    @Builder
    public TestEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
