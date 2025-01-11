package com.example.emtechelppathbackend.survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {






    boolean existsByTitle(String title);
@Query(value = "SELECT s.id AS id, s.title AS title, s.chapter_id AS chapterId, \n" +
        "s.hub_id AS hubId, q.id AS questionId, q.question AS question, a.id AS answerId, a.answer AS answer\n" +
        " FROM surveys s\n" +
        " LEFT JOIN questions q ON s.id = q.survey_id\n" +
        " LEFT JOIN answers a ON s.id =a.survey_id\n",nativeQuery = true)
    List<SurveyInterface> findAllDetails();
    @Query(value = "SELECT s.id AS id, s.title AS title, s.chapter_id AS chapterId, \n" +
            "s.hub_id AS hubId, q.id AS questionId, q.question AS question" +
            " FROM surveys s\n" +
            " LEFT JOIN questions q ON s.id = q.survey_id\n" +
            
            " WHERE s.id = :surveyId",nativeQuery = true)
    List<SurveyInterface> findSurveysById(Long surveyId);
@Query(value = "SELECT s.id AS id, s.title AS title, s.chapter_id AS chapterId, \n" +
        "            s.hub_id AS hubId, q.id AS questionId, q.question AS question\n" +
        "             FROM surveys s\n" +
        "             LEFT JOIN questions q ON s.id = q.survey_id\n" +
        "            \n" +
        "             WHERE s.id = :surveyId AND s.hub_id = :hubId",nativeQuery = true)
List<SurveyInterface> findByIdAndHubId(Long surveyId, Long hubId);
@Query(value = "SELECT s.id AS id, s.title AS title, s.chapter_id AS chapterId, \n" +
        "            s.hub_id AS hubId, q.id AS questionId, q.question AS question\n" +
        "             FROM surveys s\n" +
        "             LEFT JOIN questions q ON s.id = q.survey_id\n" +
        "            \n" +
        "             WHERE s.id = :surveyId AND s.chapter_id = :chapterId",nativeQuery = true)
    List<SurveyInterface> findByIdAndChapterId(Long surveyId, Long chapterId);
    @Query(value = "SELECT s.id AS id, s.title AS title, q.id AS questionId, q.question AS question\n" +
            "FROM surveys s \n" +
            "LEFT JOIN questions q ON s.id = q.survey_id",nativeQuery = true)
    List<SurveyInterface> findAllSurveys();


    interface  SurveyInterface{

     Long getId();
     String getTitle();
     Long getChapterId();
     Long getHubId();
     Long getQuestionId();
     String getQuestion();
    
   
}
}
