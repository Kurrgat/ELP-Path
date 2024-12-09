package com.example.emtechelppathbackend.security.resetpassword;

import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.security.user.UserService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reset-password")
public class ResetPasswordController {

    private final UserService userService;
    private final EmailService emailService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String userEmail) {
        log.info("Received userEmail {} ", userEmail);
        try {
            Users user = userService.findUsersByEmail(userEmail);
            String resetToken = ResetTokenGenerator.generateResetToken();
            user.setPasswordResetToken(resetToken);

            log.info("****Received token is {}: ", resetToken);
            usersRepository.save(user);

            emailService.sendWithOutAttachment(emailService.SetResetPasswordRequest(user));

            return new ResponseEntity<>(new ResponseRecord("Password reset mail sent successfully",
                    userService.mapUserToDto(user)), HttpStatus.OK);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token, @RequestParam String newPassword) {
        try {
            Users user = userService.findUserByResetToken(token);

            //encode the new password
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setUserPassword(encodedPassword);
            user.setPasswordResetToken(null);
            usersRepository.save(user);

            return new ResponseEntity<>(new ResponseRecord("password Reset Successfully",
                    userService.mapUserToDto(user)), HttpStatus.OK);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }
}
