package com.example.emtechelppathbackend.survey;


import com.example.emtechelppathbackend.chapter.*;
import com.example.emtechelppathbackend.hubs.hubsv2.HubsRepov2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService{
    @Autowired
    private final SurveyRepository surveyRepository;
    @Autowired
    private final ChapterRepoV2 chapterRepoV2;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final HubsRepov2 hubsRepo;
    @Autowired
    private final JavaMailSender javaMailSender;



    @Override
    public CustomResponse<SurveyEntity> saveSurvey( Long hubId,Long regionOrInstitutionId, SurveyEntity survey) {
        CustomResponse<SurveyEntity> response = new CustomResponse<>();
        try {
            ChapterV2 chapter = null;
            Hubv2 hub = null;

            if (regionOrInstitutionId != null) {
                chapter = chapterRepoV2.findById(regionOrInstitutionId).orElse(null);

            }

            if (hubId != null) {
                hub = hubsRepo.findById(hubId).orElse(null);
            }

            if (chapter == null && hub == null) {
                response.setMessage("Failed to save survey: Chapter with id " + regionOrInstitutionId + " and Hub with id " + hubId + " do not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                survey.setChapter(chapter);
                survey.setHub(hub);
                assert chapter != null;
                survey.setRegionOrInstitutionId(chapter.getRegionOrInstitutionId());



                surveyRepository.save(survey);

                String chapterType = "Chapter";
                response.setMessage("Survey saved successfully for " + chapterType);

                // Send survey via email as a link


                List<String> emails= usersRepository.findAll().stream().map(Users::getUserEmail).toList();


                if (!emails.isEmpty()) {
                    String surveyLink = generateSurveyLink(survey);
                    sendSurveyByEmail(survey, emails, surveyLink);
                    response.setMessage(response.getMessage() + " and sent via email as a link");
                }


                response.setStatusCode(HttpStatus.CREATED.value());
                response.setSuccess(true);
                response.setPayload(survey);
            }
        } catch (Exception e) {
        e.printStackTrace();
            response.setMessage("Failed to save survey: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    private void sendSurveyByEmail(SurveyEntity survey, List<String> emailAddresses, String surveyLink) {
        // Implement logic to send the survey via email to multiple recipients
        for (String emailAddress : emailAddresses) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@emtechhouse.co.ke");
            message.setTo(emailAddress);
            message.setSubject("New Survey Available");
            message.setText("Dear recipient,\n\nA new survey is available. Please check it out:\n" + surveyLink);

            javaMailSender.send(message);
        }
    }

    private String generateSurveyLink(SurveyEntity survey) {
        // Implement logic to generate a unique survey link based on the survey details
        // For example, you can use the survey ID or a UUID and append it to your survey link base URL
        String surveyLinkBaseURL = "https://surveys/";

        return surveyLinkBaseURL + survey.getId();

    }



    @Override
    public CustomResponse<List<SurveyEntity>> getSurveysForChapter(Long regionOrInstitutionId) {
        CustomResponse<List<SurveyEntity>> response = new CustomResponse<>();
        try {
            // Assuming regionOrInstitutionId is the ID of the survey you want to retrieve
            Optional<SurveyEntity> surveyOptional = surveyRepository.findById(regionOrInstitutionId);

            if (surveyOptional.isPresent()) {
                List<SurveyEntity> result = Collections.singletonList(surveyOptional.get());


                response.setMessage("Surveys retrieved successfully for Chapter");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(result);
            } else {
                response.setMessage("Survey not found for the provided ID");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Failed to retrieve surveys: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<List<SurveyEntity>> getSurveysForHub(Long hubId) {
        CustomResponse<List<SurveyEntity>> response = new CustomResponse<>();
        try {
            List<SurveyEntity> result = surveyRepository.findByHubId(hubId);

            response.setMessage("Surveys retrieved successfully for Hub");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage("Failed to retrieve surveys: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<?> deleteSurveyById(Long id) {
        CustomResponse<SurveyEntity>response=new CustomResponse<>();
        try {
           if ((surveyRepository.existsById(id))){
               surveyRepository.deleteById(id);
               response.setMessage("survey with the id"+id+"has been deleted successfully");
               response.setStatusCode(HttpStatus.OK.value());
               response.setSuccess(true);
           }else {
               response.setMessage("survey not found");
               response.setStatusCode(HttpStatus.NOT_FOUND.value());
               response.setSuccess(false);
           }

        }catch (Exception e){
            response.setMessage("failed to delete survey"+e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    public CustomResponse<?> updateSurveyById(Long id, SurveyEntity updatedSurveyEntity) {
        CustomResponse<SurveyEntity> response = new CustomResponse<>();
        try {
            if (surveyRepository.existsById(id)) {
                // Set the ID of the updated survey entity
                updatedSurveyEntity.setId(id);

                // Save the updated survey to the database
                SurveyEntity savedSurvey = surveyRepository.save(updatedSurveyEntity);


                response.setMessage("Survey updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(savedSurvey);
            } else {
                response.setMessage("Survey not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setMessage("Failed to update survey");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


}
