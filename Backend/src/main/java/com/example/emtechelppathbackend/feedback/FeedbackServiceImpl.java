package com.example.emtechelppathbackend.feedback;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedbackRepository;

   private final UsersRepository usersRepository;

    @Override
    public CustomResponse<?> createFeedback(Long userId, FeedbackDto feedbackDto) {
        CustomResponse<Feedback> response = new CustomResponse<>();

        try {
            // Load the Users entity from the repository based on the provided userId
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new Exception("User not found with id: " + userId));

            String email = user.getEmail();

            Feedback result = mapFeedbackDtoToEntity(feedbackDto, email);
            result.setUsers(user);


            // Save the feedback entity with the associated user
            result = feedbackRepository.save(result);

            response.setMessage("Feedback created successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(result);
        } catch (Exception e) {

            response.setMessage("Error creating feedback: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<?> deleteFeedbackById(Long id) {
        CustomResponse<Feedback>response=new CustomResponse<>();
        try {
            Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);

            if (feedbackOptional.isPresent()) {
                feedbackRepository.deleteById(id);

                response.setMessage("Feedback deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage("Error deleting feedback: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    // Add a method to map FeedbackDto to Feedback entity
    private Feedback mapFeedbackDtoToEntity(FeedbackDto feedbackDto, String email) {
        Feedback feedback = new Feedback();
        feedback.setEmail(email);
        feedback.setMessage(feedbackDto.getMessage());

        return feedback;
    }


    @Override
    public CustomResponse<List<Feedback>> getAllFeedback() throws NoResourceFoundException {
        CustomResponse<List<Feedback>> response = new CustomResponse<>();
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();

            if (feedbacks.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No feedback found");
                response.setPayload(null);
            } else {
                response.setPayload(feedbacks);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<Optional<Feedback>> findById(Long id) {
        CustomResponse<Optional<Feedback>>response=new CustomResponse<>();
        try {
            var result=feedbackRepository.findById(id);
            if (result.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No feedback found");
                response.setPayload(null);
            } else {
                response.setPayload(result);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public  CustomResponse<List<Feedback>> getFeedbackByDateRange(LocalDate startDate, LocalDate endDate) {
        CustomResponse<List<Feedback>>response=new CustomResponse<>();
        try {
            var result= feedbackRepository.findByDateBetween(startDate, endDate);
            if (result.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No feedback found");
                response.setPayload(null);
            } else {
                response.setPayload(result);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }




}
