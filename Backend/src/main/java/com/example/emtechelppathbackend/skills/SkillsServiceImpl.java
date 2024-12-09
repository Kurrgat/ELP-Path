package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillsServiceImpl implements SkillsService {
    private final SkillsRepository skillsRepository;
    private final UsersRepository usersRepository;

    @Override
    public CustomResponse<Skills> createSkill(SkillsDto skillsDto, Long userId) {
        CustomResponse<Skills> response = new CustomResponse<>();
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);

            if (userOptional.isEmpty()) {
                response.setMessage("User does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                Users user = userOptional.get();
                Skills skills=new Skills();
                skills.setSkillsName(skillsDto.getSkillsName());
                skills.setLevel(skillsDto.getLevel());

                skills.setUsers(user);
                skillsRepository.save(skills);
                response.setMessage("Skill created successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(skills);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<Void> deleteSkill(Long skillId) {
        CustomResponse<Void> response = new CustomResponse<>();

        try {
            Optional<Skills> skillOptional = skillsRepository.findById(skillId);

            if (skillOptional.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Skills skill = skillOptional.get();
                skillsRepository.delete(skill);
                response.setMessage("Skill deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<Void> deleteAllSkills() {
        CustomResponse<Void> response = new CustomResponse<>();

        try {
            // Find all skills in the database
            List<Skills> allSkills = skillsRepository.findAll();

            if (allSkills.isEmpty()) {
                response.setMessage("No skills found to delete");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                // Delete all skills
                skillsRepository.deleteAll(allSkills);
                response.setMessage("All skills deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<Skills> editSkill(SkillsDto updatedSkillsDto, Long userId) {
        CustomResponse<Skills> response = new CustomResponse<>();

        try {
            Optional<Skills> skillOptional = skillsRepository.findById(userId);

            if (skillOptional.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Skills skill = skillOptional.get();
                skill.setSkillsName(updatedSkillsDto.getSkillsName());
                skill.setLevel(updatedSkillsDto.getLevel());

                skillsRepository.save(skill);

                response.setMessage("Skill updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }
    @Override
    public CustomResponse<List<Skills>> getByUserId(Long userId) {
        CustomResponse<List<Skills>> response = new CustomResponse<>();

        try {
            List<Skills> skillList = skillsRepository.findByUserId(userId);

            if (skillList.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                response.setMessage("Skill retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(skillList);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }
}
