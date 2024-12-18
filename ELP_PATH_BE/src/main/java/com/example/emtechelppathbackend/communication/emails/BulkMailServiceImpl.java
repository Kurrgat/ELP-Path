package com.example.emtechelppathbackend.communication.emails;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.emtechelppathbackend.chapter.ChapterService;
import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.profile.ProfileService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BulkMailServiceImpl implements BulkMailService {
    private EmailService emailService;
    private UsersRepository usersRepository;
    private ChapterService chapterService;
    private ProfileService profileService;


    public CustomResponse<?> sendToAll(String subject, String body) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            if(usersRepository.findAll().isEmpty()) {
                response.setMessage("No recipients found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                List<Users> users = usersRepository.findAll();
                try {
                    for(Users user : users) {
                        try {
                            if(!user.getRole().getRoleName().equals("SUPER_ADMIN")) {
                             if(user.getUserEmail() != null) {
                                EmailDetails emailDetails = new EmailDetails();
                                emailDetails.setRecipient(user.getUserEmail());
                                emailDetails.setSubject(subject);
                                emailDetails.setMessageBody(body);

                                log.info("sending to .... "+user.getUserEmail());
                                emailService.sendWithOutAttachment(emailDetails);
                            }
                        }
                        } catch (Exception e) {
                            log.error("Error sending email to user "+user.getUserEmail()+" at "+LocalDateTime.now());
                        }
                    }
                        response.setMessage(HttpStatus.OK.getReasonPhrase());
                        response.setPayload("Emails sent out on "+LocalDateTime.now());
                        response.setStatusCode(HttpStatus.OK.value());
                } catch (Exception e) {
                    response.setMessage(e.getMessage());
                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setSuccess(false);
                }
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

        @Override
    public CustomResponse<?> sendToChapterMembers(Long chapterId, String subject, String body) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            var members = chapterService.getMembersByChapterId(chapterId);
            if(members.getPayload().isEmpty()) {
                response.setMessage("No members for this chapter");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                for (UsersDto usersDto: members.getPayload()) {
                    String email = profileService.getProfileByUserId(usersDto.getId()).getPayload().getProfile().getEmail();
                    if (email != null) {
                        EmailDetails emailDetails = new EmailDetails();
                        emailDetails.setRecipient(email);
                        emailDetails.setSubject(subject);
                        emailDetails.setMessageBody(body);

                        log.info("sending to .... "+email);
                        emailService.sendWithOutAttachment(emailDetails);
                    }
                }
                response.setMessage("Emails sent out successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload("Success");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }
}
