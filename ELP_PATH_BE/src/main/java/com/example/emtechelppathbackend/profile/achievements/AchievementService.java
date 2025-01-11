package com.example.emtechelppathbackend.profile.achievements;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface AchievementService {
    CustomResponse<?> addAchievements(AchievementsDto achievementsDto, Long userId);

    CustomResponse<?> getAchievementsByUserId(Long userId);

    CustomResponse<?> updateAchievements(AchievementsDto achievementsDto, Long userId, Long id);

    CustomResponse<?> deleteAchievements( Long userId,Long id);
}
