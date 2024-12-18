package com.example.emtechelppathbackend.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/get-user-notification/{userId}")
    public ResponseEntity<?>getUserNotifications(@PathVariable Long userId){
        var response=notificationService.getUserNotifications(userId);
       return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/get-user-notification/{userId}/{notificationId}")
    public ResponseEntity<?>editUserNotifications(@PathVariable Long userId, @PathVariable Long notificationId,@RequestBody NotificationDto notificationDto){
        var response=notificationService.editUserNotifications(userId,notificationId,notificationDto);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
