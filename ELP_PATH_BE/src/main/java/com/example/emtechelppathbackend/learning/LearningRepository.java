package com.example.emtechelppathbackend.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LearningRepository extends JpaRepository<LearningEntity, Long> {
}
