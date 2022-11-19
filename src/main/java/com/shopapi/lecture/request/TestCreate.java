package com.shopapi.lecture.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class TestCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;
    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder
    private TestCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /*
        빋더의 장점
        1. 가독성이 좋다.
        2. 값 생성에 대한 유연함
        3. 필요한 값만 받을 수 있다. ( 생성자로는 오버로딩 해야함 )
            -> 오버로딩 가능한 조건 : 같은 반환 타입, 같은 메서드 네임 ( 파라미터 개수만 달라짐 )
        4. 객체 불변성
     */
}
