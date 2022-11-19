package com.shopapi.lecture.service;

import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.repository.TestRepository;
import com.shopapi.lecture.request.TestCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    // @Autowired 필드 인젝션 추천 X -> 생성자 인젝션으로 사용
    private final TestRepository testRepository;

    public void saveTest(TestCreate testCreate) {
        //repository.save(test);
        TestEntity test = TestEntity.builder()
                .title(testCreate.getTitle())
                .content(testCreate.getContent())
                .build();

        testRepository.save(test);
    }

    public TestEntity saveTestReturnEntity(TestCreate request) {
        TestEntity test = TestEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return testRepository.save(test);
    }

    public Long saveTestReturnKey(TestCreate request) {
        TestEntity test = TestEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        TestEntity save = testRepository.save(test);

        return save.getId();
    }

    public TestEntity get(Long testId) {
        TestEntity test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return test;
    }
}
