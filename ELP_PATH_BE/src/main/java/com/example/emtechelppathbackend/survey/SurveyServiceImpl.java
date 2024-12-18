package com.example.emtechelppathbackend.survey;


import com.example.emtechelppathbackend.chapter.*;
import com.example.emtechelppathbackend.hubs.hubsv2.HubsRepov2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.sportlight.SportLight;
import com.example.emtechelppathbackend.survey.questions.*;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService{
private final SurveyRepository surveyRepository;
private  final ChapterRepoV2 chapterRepo;
private final HubsRepov2 hubsRepo;
private final QuestionsRepository questionsRepository;
private final AnswersRepository answersRepository;
private final UsersRepository userRepository;


    @Override
    public CustomResponse<SurveyDto> postSurvey(SurveyDto surveyDto, Long chapterId, Long hubId) {
        CustomResponse<SurveyDto> response = new CustomResponse<>();
        try {

            // Check if the survey title already exists
            if (surveyRepository.existsByTitle(surveyDto.getTitle())) {
                throw new IllegalArgumentException("Survey with the same title already exists.");
            }

            // Create and save the survey entity
            SurveyEntity surveyEntity = new SurveyEntity();
            surveyEntity.setTitle(surveyDto.getTitle());
            surveyEntity = surveyRepository.save(surveyEntity);

            // Associate and save questions with the survey entity
            List<Questions> questionsList = new ArrayList<>();
            for (int i = 0; i < surveyDto.getQuestionsList().size(); i++) {
                // Get the question text from the SurveyDto
                String questionText = surveyDto.getQuestionsList().get(i).getQuestion();

                // Create a new Questions entity and set its properties
                Questions question = new Questions();
                question.setQuestion(questionText);
                question.setSurvey(surveyEntity);

                // Save question and update list with generated ID
                question = questionsRepository.save(question);
                questionsList.add(question);
            }

            // Additional logic to associate survey with chapter or hub if needed
            if (chapterId != null) {
                Optional<ChapterV2> chapterOptional = chapterRepo.findById(chapterId);
                chapterOptional.ifPresent(surveyEntity::setChapter);
            }

            if (hubId != null) {
                Optional<Hubv2> hubOptional = hubsRepo.findById(hubId);
                hubOptional.ifPresent(surveyEntity::setHub);
            }

            // Update the survey entity after associating it with chapter or hub
             surveyRepository.save(surveyEntity);

            // Set questions and answers list to the survey DTO
            surveyDto.setQuestionsList(questionsList);
            response.setMessage("Survey created successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setSuccess(true);
            response.setPayload(surveyDto);
        } catch (IllegalArgumentException e) {
            response.setMessage("Error adding survey: " + e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage("Error adding survey: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }




    @Override
    public CustomResponse<?> deleteSurveys(Long surveyId) {
        CustomResponse<SurveyEntity> response = new CustomResponse<>();
        try {
            Optional<SurveyEntity> optionalSurvey = surveyRepository.findById(surveyId);
            if (optionalSurvey.isEmpty()) {
                response.setMessage("Survey not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                return response;
            }

            SurveyEntity survey = optionalSurvey.get();

            // First, delete all answers related to this survey
            answersRepository.deleteBySurveyId(surveyId);

            // Ensure flushing happens right after the batch delete
            answersRepository.flush();

            // Then, delete all questions of the survey
            questionsRepository.deleteBySurveyId(surveyId);

            // Ensure flushing happens right after deleting questions
            questionsRepository.flush();

            // Finally, delete the survey itself
            surveyRepository.delete(survey);

            // Flush the changes to ensure the database is in a consistent state before ending the transaction
            surveyRepository.flush();

            response.setMessage("Survey deleted successfully");
            response.setSuccess(true);
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(null);

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }


    @Override
    public CustomResponse<?> getSurveys() {
        CustomResponse<List<SurveyRepository.SurveyInterface>> response = new CustomResponse<>();
        try {
            List<SurveyRepository.SurveyInterface> surveys = surveyRepository.findAllDetails();
            if (surveys.isEmpty()) {
                response.setMessage("Surveys not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Surveys found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(surveys);
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getSurveyById( Long surveyId) {
        CustomResponse<List<SurveyRepository.SurveyInterface>> response = new CustomResponse<>();
        try {
            List<SurveyRepository.SurveyInterface> surveys = surveyRepository.findSurveysById(surveyId);
            if (surveys.isEmpty()) {
                response.setMessage("Surveys not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Surveys found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(surveys);
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getHubSurvey(Long surveyId, Long hubId) {
        CustomResponse<List<SurveyRepository.SurveyInterface>> response = new CustomResponse<>();
        try {
            List<SurveyRepository.SurveyInterface> surveys = surveyRepository.findByIdAndHubId(surveyId, hubId);
            if (surveys.isEmpty()) {
                response.setMessage("Surveys not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Surveys found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(surveys);
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getChapterSurvey(Long surveyId, Long chapterId) {
        CustomResponse<List<SurveyRepository.SurveyInterface>> response = new CustomResponse<>();
        try {
            List<SurveyRepository.SurveyInterface> surveys = surveyRepository.findByIdAndChapterId(surveyId, chapterId);
            if (surveys.isEmpty()) {
                response.setMessage("Surveys not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Surveys found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(surveys);
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<AnswersToQuestions>> addAnswersToSurvey(Long surveyId, Long userId, List<AnswersToQuestions> answers) {
        CustomResponse<List<AnswersToQuestions>> response = new CustomResponse<>();
        try {
            // Retrieve the survey entity
            Optional<SurveyEntity> surveyOptional = surveyRepository.findById(surveyId);
            if (surveyOptional.isEmpty()) {
                throw new IllegalArgumentException("Survey not found.");
            }

            Optional<Users> usersOptional = userRepository.findById(userId);
            if (usersOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found.");
            }

            SurveyEntity surveyEntity = surveyOptional.get();
            Users user = usersOptional.get();

            // Retrieve the questions associated with the survey
            List<Questions> questions = questionsRepository.findBySurveyId(surveyId);

            // Log the questions retrieved
            System.out.println("Number of questions retrieved: " + questions.size());

            // Check if there are questions associated with the survey
            if (questions.isEmpty()) {
                response.setMessage("No questions found for the survey.");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Check if the number of answers matches the number of questions
            if (answers.size() != questions.size()) {
                throw new IllegalArgumentException("Number of answers does not match number of questions.");
            }

            // List to hold saved answers
            List<AnswersToQuestions> savedAnswers = new ArrayList<>();

            // Iterate over the answers and associate them with the survey, questions, and user
            for (int i = 0; i < answers.size(); i++) {
                AnswersToQuestions answer = answers.get(i);
                Questions question = questions.get(i);

                answer.setSurvey(surveyEntity);
                answer.setQuestions(question);
                answer.setUsers(user);

                // Save the answer and add to savedAnswers list
                savedAnswers.add(answersRepository.save(answer));
            }

            response.setMessage("Answers added successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setSuccess(true);
            response.setPayload(savedAnswers); // Set the payload with the saved answers
        } catch (IllegalArgumentException e) {
            response.setMessage("Error adding answers: " + e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage("Error adding answers: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getAllSurveyQuestions() {
        CustomResponse<List<SurveyRepository.SurveyInterface>> response = new CustomResponse<>();
        try {
            List<SurveyRepository.SurveyInterface> surveys = surveyRepository.findAllSurveys();
            if (surveys.isEmpty()) {
                response.setMessage("Surveys not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Surveys found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(surveys);
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


}
