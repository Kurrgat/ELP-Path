package com.example.emtechelppathbackend.learning;

import com.example.emtechelppathbackend.learning.enrol.EnrolDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/learning")
@RequiredArgsConstructor
public class LearningController {
private final LearningService learningService;
    @PostMapping("/add-course")
    public ResponseEntity<?> addCourse(@ModelAttribute LearningDto learningDto){
        var response = learningService.addCourse(learningDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-learning-course")
    public ResponseEntity<?> getLearningCourse(){
        var response = learningService.getLearningCourse();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/edit-learning-course/{learningId}")
    public ResponseEntity<?> editLearningCourse(@ModelAttribute LearningDto learningDto, @PathVariable Long learningId){
        var response = learningService.editLearningCourse(learningDto, learningId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{learningId}")
    public ResponseEntity<?> deleteLearningCourse(@PathVariable Long learningId) {
        var response= learningService.deleteLearningCourse(learningId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/edit-learning-course/{learningId}/{userId}")
    public ResponseEntity<?> enrol(@RequestBody EnrolDto enrolDto, @PathVariable Long learningId, @PathVariable Long userId){
        var response = learningService.enrolCourse(enrolDto, learningId,userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
