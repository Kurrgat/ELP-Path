package com.example.emtechelppathbackend.application.schoolhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolHistoryRepository extends JpaRepository<SchoolHistory, Long> {

	  List <SchoolHistory> findByApplicationId(Long applicationId);
}
