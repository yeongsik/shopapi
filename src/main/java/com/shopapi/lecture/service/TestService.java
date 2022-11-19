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
}
