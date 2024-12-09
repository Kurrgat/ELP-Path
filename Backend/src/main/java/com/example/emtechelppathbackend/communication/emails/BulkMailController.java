package com.example.emtechelppathbackend.communication.emails;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bulk-mail")
@RequiredArgsConstructor
public class BulkMailController {
    private final BulkMailService bulkMailService;

    @PostMapping(value = "/send-email")
    public ResponseEntity<?> sendToAll(String subject, String body) {
        var response = bulkMailService.sendToAll(subject, body);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
