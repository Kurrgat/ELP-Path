package com.example.emtechelppathbackend.skills;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skills")
public class SkillsController {
    private final SkillsService skillsService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createSkill(@RequestBody SkillsDto skillsDto, @PathVariable Long userId) {
        var response = skillsService.createSkill(skillsDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{skillId}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long skillId) {
        var response = skillsService.deleteSkill(skillId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping("/delete/all")
    public ResponseEntity<?> deleteAllSkills() {
        var response = skillsService.deleteAllSkills();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/edit/{UserId}")
    public ResponseEntity<?> editSkill(@RequestBody SkillsDto updatedSkillsDto, @PathVariable Long userId) {
        var response = skillsService.editSkill(updatedSkillsDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getSkillById(@PathVariable Long userId) {
        var response = skillsService.getByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }





}