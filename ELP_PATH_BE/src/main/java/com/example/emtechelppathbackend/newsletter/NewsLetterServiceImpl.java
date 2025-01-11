package com.example.emtechelppathbackend.newsletter;

import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdates;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class NewsLetterServiceImpl implements NewsLetterService {

    private final NewsLetterRepo newsLetterRepo;
    private final UsersRepository usersRepository;


    @Override
    public CustomResponse<?> requestNewsletter(String email) {
        CustomResponse<NewsLetter> response = new CustomResponse<>();
        try {
            // Retrieve the user details based on userId
            Users user = usersRepository.findUsersByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            // Check if the email is already subscribed
            if (newsLetterRepo.existsByEmail(user.getEmail())) {
                response.setMessage("Email address is already subscribed to the newsletter.");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Save the subscription details
            NewsLetter newsLetter = new NewsLetter();
            newsLetter.setEmail(user.getEmail());
            newsLetter.setName(user.getFirstName() + " " + user.getLastName());
            newsLetter.setUser(user);
            newsLetterRepo.save(newsLetter);

            response.setMessage("Newsletter subscription request submitted successfully.");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(newsLetter);
        } catch (Exception e) {
            response.setMessage("Failed to process newsletter subscription request: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getNewsletterRequest() {
        CustomResponse<List<NewsLetter>> response = new CustomResponse<>();
        try {
            List<NewsLetter> letterList=newsLetterRepo.findAll();
            if (letterList.isEmpty()){
                response.setMessage("newsletters not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("newsletter retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(letterList);
            }
        } catch (Exception e) {
            response.setMessage("Failed to process newsletter subscription request: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> deleteNewsletterRequest(Long newsletterId) {
        CustomResponse<NewsLetter> response = new CustomResponse<>();
        try {
            Optional<NewsLetter> optionalNewsLetter=newsLetterRepo.findById(newsletterId);
            if ( optionalNewsLetter.isEmpty()){
                response.setMessage("newsletters not found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                newsLetterRepo.deleteById(newsletterId);
                response.setMessage("newsletter deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Failed to process newsletter subscription request: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

}
