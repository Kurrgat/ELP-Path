package com.example.emtechelppathbackend.newsletter;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface NewsLetterService {


    CustomResponse<?> requestNewsletter(String email);

    CustomResponse<?> getNewsletterRequest();

    CustomResponse<?> deleteNewsletterRequest(Long newsletterId);
}
