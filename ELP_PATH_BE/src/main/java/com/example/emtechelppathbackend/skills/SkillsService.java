package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface SkillsService {
   CustomResponse<Skills >createSkill(SkillsDto skills, Long userId);

  CustomResponse<?> deleteSkill( Long userId, Long skillId);

    CustomResponse<Void> deleteAllSkills();

   

    CustomResponse<?> getTechnicalSkillsByUserId(Long userId);

    CustomResponse<?> addLanguageSkill(SkillsLanguageDto skillsLanguageDto, Long userId);

 CustomResponse<?> addSoftSkill(SoftSkillsDto softSkillsDto, Long userId);



    CustomResponse<?> getLanguageSkillsByUserId(Long userId);

    CustomResponse<?> getSoftSkillsByUserId(Long userId);

    CustomResponse<?> editTechnicalSkill(SkillsDto updatedSkillsDto, Long userId, Long skillId);

    CustomResponse<?> editLanguageSkill(SkillsLanguageDto updatedSkillsDto, Long userId, Long skillId);

    CustomResponse<?> editSoftSkill(SoftSkillsDto updatedSkillsDto, Long userId, Long skillId);
}
