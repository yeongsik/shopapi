package com.shopapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("/api/hello 요청시 Hello 출력")
    void test() throws Exception {
        //expected
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/helloPost 요청시 Hello를 출력")
    void postTest() throws Exception {



        // application/json 형태를 많이 사용하는 추세
        mockMvc.perform(post("/api/helloPost")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title","글 제목입니다.")
                        .param("content","글 내용입니다 하하"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/helloPostClass 요청시 Hello를 출력")
    void postTestByRequestBody() throws Exception {

        /*
            글 제목
            글 내용 뿐만아니라 사용자 ( id , name , level) 등 객체가 포함되어서 전달할 경우

            APPLICATION_FORM_URLENCODED 타입 경우 key, value 형태로 풀어서 전달해야하는데
            json 은 자바스크립트 객체 형태로 데이터를 온전히 표현한다.
         */

        mockMvc.perform(post("/api/helloPostClass")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", "글제목입니다.")
                        .param("content", "글 내용입니다"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/helloPostJson 요청시 Hello를 출력")
    void postTestByJson() throws Exception {
        mockMvc.perform(post("/api/helloPostJson")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\" : \"내용입니다.\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/helloPostJson title값은 필수다.")
    void postTestByJson2() throws Exception {

        // expected
        mockMvc.perform(post("/api/helloPostJson")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": null, \"content\" : \"내용입니다.\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/validationByControllerAdvice ControllerAdvice로 검증")
    void validationByControllerAdviceTest() throws Exception {
        mockMvc.perform(post("/api/validationByControllerAdvice")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": null, \"content\" : \"내용입니다.\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }
}