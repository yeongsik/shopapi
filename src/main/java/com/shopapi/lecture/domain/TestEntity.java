package com.shopapi.lecture.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    private TestEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public TestEditor.TestEditorBuilder toEditer() {
        return TestEditor.builder()
                .title(title)
                .content(content);

    }

    public void edit(TestEditor testEditor) {
        title = testEditor.getTitle();
        content = testEditor.getContent();
    }
}
