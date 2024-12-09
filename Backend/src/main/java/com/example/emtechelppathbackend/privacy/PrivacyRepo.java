package com.example.emtechelppathbackend.privacy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivacyRepo extends JpaRepository<PrivacyEntity, Long> {
    PrivacyEntity findByUserId(Long userId);

    @Query(value = "UPDATE privacy_entity SET is_email_private=:email, is_phone_number_private=:phone WHERE user_id=:userId ",nativeQuery = true)
    void updatePrivacy(Boolean email, Boolean phone,Long userId);
}
