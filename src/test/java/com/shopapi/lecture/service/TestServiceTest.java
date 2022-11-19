package com.shopapi.lecture.service;

import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.repository.TestRepository;
import com.shopapi.lecture.request.TestCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        testRepository.save(request);
        Long testId = 1L;

        // when
        TestEntity testEntity = testService.get(request.getId());

        // then
        assertNotNull(testEntity);
        assertEquals(request.getTitle(), testEntity.getTitle());
        assertEquals(request.getContent(), testEntity.getContent());
    }
}