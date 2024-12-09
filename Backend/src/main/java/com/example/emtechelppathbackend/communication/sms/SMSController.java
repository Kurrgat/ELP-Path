package com.example.emtechelppathbackend.communication.sms;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sms")
public class SMSController {
    private final SMSService smsService;

    @PostMapping(value = "/sendSms")
    public ResponseEntity<?> sendSms(@RequestBody SMSBODY smsbody) {
        var result = smsService.sendSms(smsbody);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
