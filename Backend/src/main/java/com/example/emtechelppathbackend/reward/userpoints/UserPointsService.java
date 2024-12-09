package com.example.emtechelppathbackend.reward.userpoints;

import com.example.emtechelppathbackend.utils.CustomResponse;

public interface UserPointsService {
    CustomResponse<?>redeemPoints(Long userId, Long rewardId);

    CustomResponse<?>awardPoints(Long userId, Long eventId,Long activityId);

}
