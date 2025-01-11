package com.example.emtechelppathbackend.survey.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    @Query(value = "SELECT * FROM questions WHERE survey_id = :surveyId" ,nativeQuery = true)
    List<Questions> findBySurveyId(Long surveyId);

    @Query(value = "delete from questions WHERE survey_id = :surveyId", nativeQuery = true)
    void deleteBySurveyId(Long surveyId);
}
