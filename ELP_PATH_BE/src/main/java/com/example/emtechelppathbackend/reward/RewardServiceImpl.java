package com.example.emtechelppathbackend.reward;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService{
    @Autowired
    private final RewardRepo rewardRepo;
    @Override
    public CustomResponse<?> createReward(RewardEntity reward) {
        CustomResponse<RewardEntity>response=new CustomResponse<>();
        try {
            var result=rewardRepo.save(reward);
            response.setMessage("Reward created successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(result);
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage("Failed to create reward"+e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
      return response;
    }
}
