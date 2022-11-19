package com.shopapi.lecture.repository;

import com.shopapi.lecture.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity,Long> {
}
