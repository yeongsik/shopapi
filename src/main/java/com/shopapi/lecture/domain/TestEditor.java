package com.shopapi.lecture.domain;

import lombok.Getter;

@Getter
public class TestEditor {
    // 수정할 데이터만 필드로 만들어준다.

    private final String title;
    private final String content;
    public TestEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static TestEditorBuilder builder() {
        return new TestEditorBuilder();
    }

    public static class TestEditorBuilder {
        private String title;
        private String content;

        TestEditorBuilder() {
        }

        public TestEditorBuilder title(final String title) {
            if (title != null) {
                this.title = title;
            }
            return this;
        }

        public TestEditorBuilder content(final String content) {
            if (content != null) {
                this.content = content;
            }
            return this;
        }

        public TestEditor build() {
            return new TestEditor(this.title, this.content);
        }

        public String toString() {
            return "TestEditor.TestEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }

}
