//package com.example.emtechelppathbackend.feedback;
//
//import com.example.emtechelppathbackend.utils.CustomResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public interface FeedbackService {
//    Feedback createFeedback(Feedback feedback);
//
//   CustomResponse<List<Feedback>> getAllFeedback();
//}
package com.example.emtechelppathbackend.feedback;

import com.example.emtechelppathbackend.utils.CustomResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    CustomResponse<List<Feedback>> getAllFeedback();

    CustomResponse<Optional<Feedback>> findById(Long id);

    CustomResponse<List<Feedback>> getFeedbackByDateRange(LocalDate startDate, LocalDate endDate);

    CustomResponse<?> createFeedback(Long userId, FeedbackDto feedbackDto);

    CustomResponse<?>deleteFeedbackById(Long id);
}
