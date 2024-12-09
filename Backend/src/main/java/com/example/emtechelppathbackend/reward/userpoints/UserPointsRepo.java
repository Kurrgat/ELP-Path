package com.example.emtechelppathbackend.reward.userpoints;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPointsRepo extends JpaRepository<UserPoints,Long> {
    Optional<UserPoints> findByUserId(Long userId);
}
