package com.shopapi.lecture.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopapi.lecture.domain.QTestEntity;
import com.shopapi.lecture.domain.TestEntity;
import com.shopapi.lecture.request.TestSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TestRepositoryImpl implements TestRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TestEntity> getList(TestSearch testSearch) {
        return jpaQueryFactory.selectFrom(QTestEntity.testEntity)
                .limit(testSearch.getSize())
                .offset(testSearch.getOffset())
                .orderBy(QTestEntity.testEntity.id.desc())
                .fetch();
    }
}
