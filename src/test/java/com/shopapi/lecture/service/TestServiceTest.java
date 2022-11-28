package com.shopapi.lecture.service;

import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.repository.TestRepository;
import com.shopapi.lecture.request.TestCreate;
import com.shopapi.lecture.request.TestEdit;
import com.shopapi.lecture.request.TestSearch;
import com.shopapi.lecture.response.TestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Autowired
    private TestRepository testRepository;

    @BeforeEach
    void clean() {
        testRepository.deleteAll();
    }
    @Test
    @DisplayName(" 테스트 작성")
    void test1() {
        // given
        TestCreate testCreate = TestCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        testService.saveTest(testCreate);

        // then
        assertEquals(1L, testRepository.count());
        TestEntity testEntity = testRepository.findAll().get(0);
        assertEquals("제목입니다.", testEntity.getTitle());
        assertEquals("내용입니다.", testEntity.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        // 조회 형식의 테스트 할 때는 우선 저장해줘야 한다.
        TestEntity request = TestEntity.builder()
                .title("12345")
                .content("내용입니다.")
                .build();
        testRepository.save(request);

        // 나중에 이런 요구사항이 올 수 있다.
        /*
            클라이언트 요구사항
                json 응답에서 title값 길이를 최대 10글자로 해주세요. ( 이런 처리는 클라에서 하는 게 좋다 )
                but 서버에서 해야만 한다면?
                1. Entity에 getTitle 메소드 변경
                    return title.substring(0,10);
                    => 나중에 어떤 특정 기능이 추가가 되었을 때 10글자 정책과 충돌이 발생될 때? entity 클래스를 또 변경해야하는가??
                    => 이러한 이유 때문에 Entity 클래스에는 서비스 정책 넣지 말기 (Never)
                    서로 다른 개발자가 헷갈리게 됨
         */

        // when
        TestResponse response = testService.get(request.getId());

        // then
        assertNotNull(response);
        assertEquals(request.getId(), response.getId());
        assertEquals(request.getTitle(), "12345");
        assertEquals(request.getContent(), response.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void testGetList() {
        //given
        List<TestEntity> requestTests = IntStream.range(0, 20)
                .mapToObj(i -> TestEntity.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());
        testRepository.saveAll(requestTests);
        /*
            IntStream.range(0,30) - 아래 for문과 동일하다고 볼 수 있다.
            for(int i = 0; i < 30; i++) {
            }
         */

        // List.of - > Java 9 부터 생긴 문법 ->
        //sql = select, limit , offset 필수

        TestSearch testSearch = TestSearch.builder()
                .page(1)
                .build();
        //when
        List<TestResponse> list = testService.getList(testSearch);

        //then
        assertEquals(10L,list.size());
        assertEquals("제목 19",list.get(0).getTitle());
        assertEquals("제목 15",list.get(4).getTitle());
    }
    @Test
    @DisplayName("글 제목 수정")
    void modifyTitleTest () throws Exception {

        //given
        TestEntity test = TestEntity.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        testRepository.save(test);

        TestEdit testEdit = TestEdit.builder()
                .title("호돌걸")
                .content("반포자")
                .build();

        //when
        testService.editByEditor(test.getId() , testEdit);

        //then
        TestEntity changedTest = testRepository.findById(test.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + test.getId()));
        assertEquals("호돌걸" , changedTest.getTitle());
        assertEquals("반포자이" , changedTest.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void modifyContentTest () throws Exception {

        //given
        TestEntity test = TestEntity.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        testRepository.save(test);

        TestEdit testEdit = TestEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        //when
        testService.editByEditor(test.getId() , testEdit);

        //then
        TestEntity changedTest = testRepository.findById(test.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + test.getId()));
        assertEquals("호돌맨" , changedTest.getTitle());
        assertEquals("초가집" , changedTest.getContent());
    }
}