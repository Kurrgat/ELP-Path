package com.example.emtechelppathbackend.privacy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivacyRepo extends JpaRepository<PrivacyEntity, Long> {
   PrivacyEntity findByUserId(Long userId);


   @Query(value = "select * from privacy WHERE user_id= :userId ",nativeQuery = true)
    Optional<PrivacyEntity> findPrivacyByUserId( Long userId);
}
