package com.shopapi.lecture.repository;

import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.request.TestSearch;

import java.util.List;

public interface TestRepositoryCustom {

    List<TestEntity> getList(TestSearch testSearch); // 다른 처리가 필요할 수 있으므로 페이징 처리를 위한 리퀘스트 클래스를 생성하자
}
