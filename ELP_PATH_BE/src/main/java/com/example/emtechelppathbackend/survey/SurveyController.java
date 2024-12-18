package com.example.emtechelppathbackend.survey;


import com.example.emtechelppathbackend.survey.questions.AnswersToQuestions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {
    private final SurveyService surveyService;
    @PostMapping("/add")
    public ResponseEntity<?> postSurvey(@RequestBody SurveyDto surveyDto, @RequestParam(required = false) Long chapterId, @RequestParam(required = false) Long hubId){
        var response = surveyService.postSurvey(surveyDto,chapterId, hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/delete-surveys/{surveyId}")
    public ResponseEntity<?> deleteSurveys(@PathVariable Long surveyId){
        var response = surveyService.deleteSurveys(surveyId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-surveys-with-answers")
    public ResponseEntity<?> getSurveys(){
        var response = surveyService.getSurveys();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get/{surveyId}")
    public ResponseEntity<?> getSurveyById(@PathVariable Long surveyId){
        var response = surveyService.getSurveyById(surveyId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/view-hub-survey/{surveyId}/{hubId}")
    public ResponseEntity<?> getHubSurvey(@PathVariable Long surveyId, @PathVariable Long hubId){
        var response = surveyService.getHubSurvey(surveyId,hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/view-chapter-survey/{surveyId}/{chapterId}")
    public ResponseEntity<?> getChapterSurvey(@PathVariable Long surveyId, @PathVariable Long chapterId){
        var response = surveyService.getChapterSurvey(surveyId,chapterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/add-answers/{surveyId}/{userId}")
    public ResponseEntity<?> addAnswersToSurvey(@PathVariable Long surveyId, @PathVariable Long userId, @RequestBody List<AnswersToQuestions> answers) {

        var response = surveyService.addAnswersToSurvey(surveyId, userId, answers);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get/all-surveys-questions")
    public ResponseEntity<?> getAllSurveyQuestions(){
        var response = surveyService.getAllSurveyQuestions();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
