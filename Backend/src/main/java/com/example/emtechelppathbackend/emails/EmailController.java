package com.example.emtechelppathbackend.emails;

import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-email")
    public String sendMail(@RequestBody EmailDetails emailDetails){

        return emailService.sendWithOutAttachment(emailDetails);
    }
    @PostMapping("/send-attached-email")
    public ResponseEntity<?> sendAttachedMail(@RequestBody EmailDetails emailDetails){
        try {
            emailService.sendMailWithAttachment(emailDetails);
            return ResponseEntity.ok("email with attachment sent successfully");
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }










}
