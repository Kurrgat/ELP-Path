package com.example.emtechelppathbackend.feedback;


import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;


    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createFeedback(@PathVariable Long userId, @RequestBody FeedbackDto feedbackDto) {
            var response= feedbackService.createFeedback(userId,feedbackDto);
            return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/all")
    public ResponseEntity<CustomResponse<List<Feedback>>> getAllFeedback() {

        var response = feedbackService.getAllFeedback();
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Optional<Feedback>>> findById(Long id){
        var response= feedbackService.findById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/filter-date")
    public ResponseEntity<CustomResponse<List<Feedback>>> filterUserFeedbackByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        CustomResponse<List<Feedback>> response = feedbackService.getFeedbackByDateRange(startDate, endDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}/deleting")
    public ResponseEntity<?> deleteFeedbackById(@PathVariable Long id) {
        var response= feedbackService.deleteFeedbackById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}