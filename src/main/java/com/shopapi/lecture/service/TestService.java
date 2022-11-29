package com.shopapi.lecture.service;

import com.shopapi.lecture.domain.TestEditor;
import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.repository.TestRepository;
import com.shopapi.lecture.request.TestCreate;
import com.shopapi.lecture.request.TestEdit;
import com.shopapi.lecture.request.TestSearch;
import com.shopapi.lecture.response.TestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

        return new TestResponse(test);
    }


    // 글이 너무 많은 경우에 비용이 많이 든다. => 페이징 처리
    // 글이 1억개 일 경우 DB에서 글을 모두 조회하는 경우 DB가 다운될 수도 있다.
    // 트래픽 비용이 많이 발생 -> 데이터 전체를 조회하는 경우는 거의 없다.
    // 카테고리 같은 많지 않은 데이터의 경우 다 가지고 오는 경우가 있다.

    public List<TestResponse> getList(TestSearch testSearch) {

        // 아래 web -> page 1 -> 0으로 변경
        /*
                data:
                    web:
                      pageable:
                        one-indexed-parameters: true
         */
        // Pageable pageable = PageRequest.of(page, 5 , Sort.by(Sort.Direction.DESC, "id"));
        // 수동으로 해봤자 의미 없다.

        // 기본으로 아이디 값에 오름차순 정렬도 페이징 처리가 된다.
        // PageRequest.of 뒤에 sort 설정
        return testRepository.getList(testSearch).stream()
                .map(TestResponse::new)
                .collect(Collectors.toList());
        // 빌더 코드들이 굉장히 많아 지기 때문에 -> 반복 작업 증가로 볼 수 도 있다.
        // Response 클래스 안에서 생성자 오버로딩으로 처리
        // 궁금점
    }

    // 글 수정
    @Transactional
    public void edit(Long id, TestEdit testEdit) {
        TestEntity testEntity = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));


        // 파라미터가 많아지면 실수할 확률이 높아진다.
        testEntity.change(testEdit.getTitle(), testEdit.getContent());
    }

    // 호돌맨이 자주 쓰는 패턴 // 에디터 클래스 이해가 어려우면 위의 방법을 사용하자 ( 빌더 패턴 공부 )
    @Transactional
    public void editByEditor(Long id, TestEdit testEdit) {
        TestEntity testEntity = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        // 빌더 에디터 빌더 호출 ( 기존 데이터 )
        // testEditor 빌더 필드에 기존 데이터가 들어가 있는 상황
        TestEditor.TestEditorBuilder editorBuilder = testEntity.toEditer();

        // 빌더 필드에 수정 값 적용 후 testEditor 필드에 빌드
        // 빌더 클래스를 커스터마이징 하지 않는다면 기존 값으로 만든 에디터 클래스 필드 값이 다시 testEdit 값으로 업데이트 된다.
        // 에디터 빌더 클래스에 필드를 세팅해주는 메서드에 null 체크 조건 추가 필요
        TestEditor testEditor = editorBuilder.title(testEdit.getTitle())
                .content(testEdit.getContent())
                .build();

        testEntity.edit(testEditor);
    }
}
