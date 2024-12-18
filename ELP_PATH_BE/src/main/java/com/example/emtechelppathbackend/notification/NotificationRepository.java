package com.example.emtechelppathbackend.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value ="SELECT * FROM notification WHERE user_id = :userId", nativeQuery = true)
    List<Notification> findAllByUserId(Long userId);
    @Query(value ="SELECT * FROM notification WHERE user_id = :userId AND id = :notificationId", nativeQuery = true)
    Optional<Notification> findByRecipientIdAndId(Long userId, Long notificationId);
}
