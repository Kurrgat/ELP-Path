package com.example.emtechelppathbackend.reward.userpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userPoints")
public class UserPointsController {
    @Autowired
    private final UserPointsService userPointsService;

    @PostMapping("/redeem")
    public ResponseEntity<?> redeemPoints(@RequestParam Long userId, @RequestParam Long rewardId) {
        var response = userPointsService.redeemPoints(userId, rewardId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/award")
    public ResponseEntity<?> awardPoints( Long userId , @RequestParam(name = "eventId", required = false) Long eventId, @RequestParam(name = "activityId", required = false) Long activityId) {
        var response = userPointsService.awardPoints(userId, eventId, activityId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}