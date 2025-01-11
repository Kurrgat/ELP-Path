package com.example.emtechelppathbackend.notification;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    CustomResponse<?> getUserNotifications(Long userId);

    CustomResponse<?> editUserNotifications(Long userId, Long notificationId,NotificationDto notificationDto);
}
