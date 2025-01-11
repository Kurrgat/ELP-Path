package com.example.emtechelppathbackend.profile.achievements;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementsRepository extends JpaRepository< Achievements, Long> {

    long countByUser(Users user);
    @Query(value = "SELECT * FROM achievements WHERE users_id= :userId", nativeQuery = true)
    List<Achievements> findByUserId(Long userId);
    @Query(value = "SELECT * FROM achievements WHERE users_id= :userId And id= :id", nativeQuery = true)
    Optional<Achievements> findByUserIdAndId(Long userId, Long id);

    @Query(value = "SELECT * FROM achievements a WHERE a.name = :name AND a.users_id = :id", nativeQuery = true)
    Optional<Achievements> findByNameAndUserId(String name, Long id);
}
