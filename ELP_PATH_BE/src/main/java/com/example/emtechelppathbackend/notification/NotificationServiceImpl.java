package com.example.emtechelppathbackend.notification;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public CustomResponse<?> getUserNotifications(Long userId) {
        CustomResponse<List<Notification>> response = new CustomResponse<>();
        try {
            List<Notification> notificationList = notificationRepository.findAllByUserId(userId);
            if (notificationList.isEmpty()) {
                response.setMessage(" no notification found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Notification retrieved successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(notificationList);
            }

        } catch (Exception e) {
            response.setMessage("Unable to get notifications " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
        return response;
    }
    @Override
    public CustomResponse<?> editUserNotifications(Long userId, Long notificationId, NotificationDto notificationDto) {
        CustomResponse<Notification> response = new CustomResponse<>();
        try {
            Optional<Notification> optional = notificationRepository.findByRecipientIdAndId(userId, notificationId);
            if (optional.isEmpty()) {
                response.setMessage("No notification found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                Notification notification = optional.get();

                // Update the notification fields using data from the NotificationDto
                notification.setViewed(notificationDto.isViewed()); // Set isRead to true

                // Add other fields to update similarly

                notificationRepository.save(notification);

                response.setMessage("Notification updated successfully");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(notification);
            }
        } catch (Exception e) {
            response.setMessage("Unable to update notifications: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
        return response;
    }

}
