package com.shopapi.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.repository.TestRepository;
import com.shopapi.lecture.request.TestCreate;
import com.shopapi.lecture.request.TestEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRepository testRepository;

    @BeforeEach
    void clean() {
        testRepository.deleteAll();
    }

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
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다 하하"))
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

    @Test
    @DisplayName("/api/tests 요청시 DB에 값 저장")
    void test3() throws Exception {
        // before
//        testRepository.deleteAll(); // 지저분한 방식 -> @BeforeEach 사용

        //given
        TestCreate request = TestCreate.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        // objectMapper 설정을 변경할 때 새로운 빈 생성하자
        System.out.println(json);

        //when
        mockMvc.perform(post("/api/tests")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, testRepository.count());

        TestEntity test = testRepository.findAll().get(0);
        assertEquals("제목입니다" , test.getTitle());
        assertEquals("내용입니다." , test.getContent());
    }

    @Test
    @DisplayName("/api/tests/returnEntity 로 포스트 요청이 왔을 때 TestEntity 리턴")
    void postReturnEntity() throws Exception {

        //given
        TestCreate request = TestCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/tests/returnEntity")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andDo(print());

        TestEntity validTest = testRepository.findAll().get(0);
        assertEquals("제목입니다.", validTest.getTitle());
        assertEquals("내용입니다.", validTest.getContent());


    }

    @Test
    @DisplayName("/api/tests/returnPrimaryKey 로 포스트 요청이 왔을 때 primary_id 리턴")
    void postReturnKey() throws Exception {
        // given
        TestCreate request = TestCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/api/tests/returnPrimaryKey")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testId").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/tests/{testId}로 get 요청시 글 1건 조회")
    void test4() throws Exception {
        //given
        TestEntity testEntity = TestEntity.builder()
                .title("12345")
                .content("내용입니다.")
                .build();
        testRepository.save(testEntity);

        //expected ( when 과 then 을 섞은 의미 )
        mockMvc.perform(get("/api/tests/{testId}", testEntity.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEntity.getId()))
                .andExpect(jsonPath("$.title").value("12345"))
                .andExpect(jsonPath("$.content").value("내용입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/api/tests 접근시 글 여러개 조회")
    void testGetList() throws Exception {

        // given

        List<TestEntity> requestTests = IntStream.range(0, 20)
                .mapToObj(i -> TestEntity.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());
        testRepository.saveAll(requestTests);

        //expected
        mockMvc.perform(get("/api/tests?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                /*
                JSON 응답형태가 List형태로 내려진다. 단건 조회처럼 검증하면 안된다.
                 */
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$.[0].id").value(20))
                .andExpect(jsonPath("$.[0].title").value("제목 19"))
                .andExpect(jsonPath("$.[0].content").value("내용 19"))
                .andDo(print());

    }

    @Test
    @DisplayName("/api/tests/{testId} 글 제목 수정 테스트")
    void testTitleModify() throws Exception {

        // given
        TestEntity test = TestEntity.builder()
                .title("홍길동")
                .content("테스트 내용")
                .build();

        TestEntity save = testRepository.save(test);

        TestEdit request = TestEdit.builder()
                .title("홍길동2")
                .content("테스트 내용")
                .build();

        //expected
        mockMvc.perform(patch("/api/tests/{testId}" , save.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deleteTest () throws Exception {

        //given
        TestEntity test = TestEntity.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        testRepository.save(test);

        //expected
        mockMvc.perform(delete("/api/tests/{testId}", test.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }
}