package com.example.emtechelppathbackend.survey;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {
    @Autowired
    private final SurveyService surveyService;

    @PostMapping("/onboarding")
    public ResponseEntity<?> saveSurvey( @RequestParam(name = "regionOrInstitutionId", required = false) Long regionOrInstitutionId,
                                         @RequestParam(name = "hubId", required = false) Long hubId, @RequestBody SurveyEntity survey) {
       var response= surveyService.saveSurvey( hubId,regionOrInstitutionId,survey);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/chapter/{regionOrInstitutionId}")
    public ResponseEntity<?> getSurveysForChapter(@PathVariable Long regionOrInstitutionId) {
        var response=surveyService.getSurveysForChapter(regionOrInstitutionId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/hub/{hubId}")
    public ResponseEntity<?> getSurveysForHub(@PathVariable Long hubId) {
        var response= surveyService.getSurveysForHub(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
       var response=surveyService.deleteSurveyById(id);
       return ResponseEntity.status(response.getStatusCode()).body(response);

        }
    @PutMapping("/{id}/edit")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody SurveyEntity updatedSurveyEntity) {
        var response = surveyService.updateSurveyById(id, updatedSurveyEntity);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
