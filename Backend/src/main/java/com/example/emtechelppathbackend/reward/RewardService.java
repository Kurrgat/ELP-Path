package com.example.emtechelppathbackend.reward;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface RewardService {
    CustomResponse<?>createReward(RewardEntity reward);
}
