package com.example.emtechelppathbackend.survey;

import com.example.emtechelppathbackend.survey.questions.AnswersToQuestions;
import com.example.emtechelppathbackend.survey.questions.Questions;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public interface SurveyService {


    CustomResponse<?> postSurvey(SurveyDto surveyDto, Long chapterId, Long hubId);

  




    CustomResponse<?> deleteSurveys(Long surveyId);

    CustomResponse<?> getSurveys();

    CustomResponse<?> getSurveyById( Long surveyId);

    CustomResponse<?> getHubSurvey(Long surveyId, Long hubId);

    CustomResponse<?> getChapterSurvey(Long surveyId, Long chapterId);


    CustomResponse<?> addAnswersToSurvey(Long surveyId, Long userId, List<AnswersToQuestions> answers);

    CustomResponse<?> getAllSurveyQuestions();
}
