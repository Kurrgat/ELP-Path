package com.example.emtechelppathbackend.skills;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skills")
public class SkillsController {
    private final SkillsService skillsService;

    @PostMapping("/create/technical-skill/{userId}")
    public ResponseEntity<?> addTechnicalSkill(@RequestBody SkillsDto skillsDto, @PathVariable Long userId) {
        var response = skillsService.createSkill(skillsDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/add/language-skill/{userId}")
    public ResponseEntity<?> addLanguageSkill(@RequestBody SkillsLanguageDto skillsLanguageDto, @PathVariable Long userId) {
        var response = skillsService.addLanguageSkill(skillsLanguageDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/add/soft-skill/{userId}")
    public ResponseEntity<?> addSoftSkill(@RequestBody SoftSkillsDto softSkillsDto, @PathVariable Long userId) {
        var response = skillsService.addSoftSkill(softSkillsDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}/{skillId}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long userId, @PathVariable Long skillId) {
        var response = skillsService.deleteSkill( userId,skillId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping("/delete/all")
    public ResponseEntity<?> deleteAllSkills() {
        var response = skillsService.deleteAllSkills();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/edit-technical-skill/{userId}/{skillId}")
    public ResponseEntity<?> editTechnicalSkill(@RequestBody SkillsDto updatedSkillsDto, @PathVariable Long userId, @PathVariable Long skillId) {
        var response = skillsService.editTechnicalSkill(updatedSkillsDto, userId, skillId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/edit-language-skill/{userId}/{skillId}")
    public ResponseEntity<?> editLanguageSkill(@RequestBody SkillsLanguageDto  updatedSkillsDto, @PathVariable Long userId, @PathVariable Long skillId) {
        var response = skillsService.editLanguageSkill(updatedSkillsDto, userId,skillId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/edit-soft-skill/{userId}/{skillId}")
    public ResponseEntity<?> editSoftSkill(@RequestBody SoftSkillsDto  updatedSkillsDto, @PathVariable Long userId, @PathVariable Long skillId) {
        var response = skillsService.editSoftSkill(updatedSkillsDto, userId,skillId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-technical-skills/{userId}")
    public ResponseEntity<?> getTechnicalSkillsByUserId(@PathVariable Long userId ) {
        var response = skillsService.getTechnicalSkillsByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-language-skills/{userId}")
    public ResponseEntity<?> getLanguageSkillsByUserId(@PathVariable Long userId ) {
        var response = skillsService.getLanguageSkillsByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-soft-skills/{userId}")
    public ResponseEntity<?> getSoftSkillsByUserId(@PathVariable Long userId ) {
        var response = skillsService.getSoftSkillsByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }





}