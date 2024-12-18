package com.example.emtechelppathbackend.profile.achievements;

import com.example.emtechelppathbackend.skills.SkillsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/achievements")
public class AchievementsController {
    private final  AchievementService achievementService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addAchievements(@RequestBody AchievementsDto achievementsDto, @PathVariable Long userId) {
        var response = achievementService.addAchievements(achievementsDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getAchievementsByUserId( @PathVariable Long userId) {
        var response = achievementService.getAchievementsByUserId( userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/update/{userId}/{id}")
    public ResponseEntity<?> updateAchievements(@RequestBody AchievementsDto achievementsDto, @PathVariable Long userId, @PathVariable Long id) {
        var response = achievementService.updateAchievements(achievementsDto, userId, id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping("/delete/{userId}/{id}")
    public ResponseEntity<?> deleteAchievements( @PathVariable Long userId, @PathVariable Long id) {
        var response = achievementService.deleteAchievements( userId,id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
