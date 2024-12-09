package com.example.emtechelppathbackend.profile.profiletracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/profile-progress")
@AllArgsConstructor
public class ProfileProgressController {

    ProfileProgressService profileProgressService;
    UpdateReminderService updateReminderService;

    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<?> getProfileUpdateStatus(@PathVariable Long userId) {
        var response = profileProgressService.getProfileUpdateStatus(userId);
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(value = "/send/{userId}/send-profile-update-reminder")
    public String sendProfileUpdateEmail(@PathVariable Long userId) {
        var response = updateReminderService.profileUpdateEmailReminder(userId);
        return response;
    }

}
