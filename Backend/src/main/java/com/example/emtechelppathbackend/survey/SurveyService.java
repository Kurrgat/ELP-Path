package com.example.emtechelppathbackend.survey;

import com.example.emtechelppathbackend.utils.CustomResponse;

public interface SurveyService {
    CustomResponse<?> saveSurvey(Long hubId,Long regionOrInstitutionId, SurveyEntity survey);


    CustomResponse<?> getSurveysForChapter(Long regionOrInstitutionId);

    CustomResponse<?> getSurveysForHub(Long hubId);

    CustomResponse<?> deleteSurveyById(Long id);

    CustomResponse<?> updateSurveyById(Long id, SurveyEntity updatedSurveyEntity);


}
