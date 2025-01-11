package com.example.emtechelppathbackend.newsletter;

import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdatesDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/NewsLetter")
@RequiredArgsConstructor
public class NewsLetterController {
    @Autowired
    private  final NewsLetterService newsLetterService;

    @PostMapping("/request-newsletter/{email}")
    public ResponseEntity<?>requestNewsletter(@PathVariable String email){
        var response = newsLetterService.requestNewsletter(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-newsletter-request")
    public ResponseEntity<?>getNewsletterRequest(){
        var response = newsLetterService.getNewsletterRequest();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping("/delete-newsletter-request/{newsletterId}")
    public ResponseEntity<?>deleteNewsletterRequest(@PathVariable Long newsletterId){
        var response = newsLetterService.deleteNewsletterRequest(newsletterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }









}
