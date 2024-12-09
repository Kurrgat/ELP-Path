package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface SkillsService {
   CustomResponse<Skills >createSkill(SkillsDto skills, Long userId);

  CustomResponse<Void> deleteSkill(Long skillId);

    CustomResponse<Void> deleteAllSkills();

    CustomResponse<Skills> editSkill(SkillsDto updatedSkillsDto, Long userId);

    CustomResponse<?> getByUserId(Long userId);
}
