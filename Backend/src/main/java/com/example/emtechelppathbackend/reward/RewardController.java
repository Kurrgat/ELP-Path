package com.example.emtechelppathbackend.reward;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {
@Autowired
    private final RewardService rewardService;


    @PostMapping("/create")
    public ResponseEntity<?> createReward(@RequestBody RewardEntity reward) {
        var response= rewardService.createReward(reward);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
