package com.example.emtechelppathbackend.otp;

import com.example.emtechelppathbackend.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequiredArgsConstructor
public class OtpController {

    @Autowired
    private final OtpService otpService;


    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestParam(name="email", required = false) String email ,@RequestParam(name="phoneNumber", required = false) String phoneNumber) {
        var otp = otpService.generateOtp(email, phoneNumber);
        return ResponseEntity.status(otp.getStatusCode()).body(otp);

    }



    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam(name="email", required = false) String email ,@RequestParam(name="phoneNumber", required = false) String phoneNumber, @RequestParam("userOtp") String otp) {

        var otpVerificationResponse = otpService.verifyOtp( email ,phoneNumber, otp);
        return ResponseEntity.status(otpVerificationResponse.getStatusCode()).body(otpVerificationResponse);
    }

    @GetMapping("/get-email-phone/{userId}")
    public ResponseEntity<?> getPhoneEmail(@PathVariable Long userId) {
        var response = otpService.getPhoneEmail(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
