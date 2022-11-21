package com.shopapi.lecture.service;

import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.repository.TestRepository;
import com.shopapi.lecture.request.TestCreate;
import com.shopapi.lecture.response.TestResponse;
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

    public TestResponse get(Long testId) {
        TestEntity test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));


        // response 클래스로 변환 작업을 여기서 하는것이 맞는가??
        // 호돌맨의 경우 서비스를 두 가지로 나눈다. WebService 와 Service
        // response 에 대한 서비스 호출은 WebService에서 처리 다른 서버와 통신은 Service
        TestResponse response = TestResponse.builder()
                .id(test.getId())
                .title(test.getTitle())
                .content(test.getContent())
                .build();
        return response;
    }
}
