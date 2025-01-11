package com.example.emtechelppathbackend.survey.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswersRepository extends JpaRepository<AnswersToQuestions,Long> {
    @Query(value = "select * from answers where survey_id = :surveyId", nativeQuery = true)
    List<AnswersToQuestions> findBySurveyId(Long surveyId);



    @Query(value = "delete from answers WHERE survey_id = :surveyId", nativeQuery = true)
    void deleteBySurveyId(Long surveyId);
}
