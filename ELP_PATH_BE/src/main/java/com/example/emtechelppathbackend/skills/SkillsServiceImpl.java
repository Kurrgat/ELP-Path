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
                return response;
            }

            Users user = userOptional.get();

            // Check if the skillsName already exists for the user
            Skills existingSkill = skillsRepository.findBySkillsNameAndUsers(skillsDto.getSkillsName(), user);
            if (existingSkill != null) {
                response.setMessage("Skill with the same name already exists for the user");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Retrieve existing technical skills for the user
            long existingTechnicalSkills = skillsRepository.countTechnicalSkillsByUserId(userId);

            // Check if the user already has five technical skills
            if (existingTechnicalSkills >= 5) {
                response.setMessage("User has already reached the limit of five technical skills");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            Skills skills = new Skills();
            skills.setSkillsName(skillsDto.getSkillsName());
            skills.setTechnicalLevel(skillsDto.getTechnicalLevel());
            skills.setUsers(user);

            skillsRepository.save(skills);
            response.setMessage("Skill created successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(skills);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    @Override
    public CustomResponse<?> deleteSkill(Long userId, Long skillId) {
        CustomResponse<Skills> response = new CustomResponse<>();

        try {
            Optional<Skills>skillsOptional = skillsRepository.findByUsersAndId(userId, skillId);



            if (skillsOptional.isPresent()) {
                skillsRepository.deleteById(skillId);
                response.setMessage("Skill deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            } else {
                response.setMessage("Skill not found or count is not 1");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
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
    public CustomResponse<?> getTechnicalSkillsByUserId(Long userId) {
        CustomResponse<List<SkillsRepository.TechnicalInterface>> response = new CustomResponse<>();

        try {
            List<SkillsRepository.TechnicalInterface> skillList = skillsRepository.findTechnicalLevelByUserId(userId);

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

    @Override
    public CustomResponse<?> addLanguageSkill(SkillsLanguageDto skillsLanguageDto, Long userId) {
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
                // Check if the skillsName already exists for the user
                Skills existingSkill = skillsRepository.findBySkillsNameAndUsers(skillsLanguageDto.getSkillsName(), user);
                if (existingSkill != null) {
                    response.setMessage("Skill with the same name already exists for the user");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }
                // Count existing language skills for the user
                long existingLanguageSkillsCount = skillsRepository.countLanguageSkillsByUserId(userId);

                // Check if the user has already reached the limit of three language skills
                if (existingLanguageSkillsCount >= 3) {
                    response.setMessage("User has already reached the limit of three language skills");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }

                Skills skills = new Skills();
                skills.setSkillsName(skillsLanguageDto.getSkillsName());
                skills.setLanguageLevel(skillsLanguageDto.getLanguageLevel());
                skills.setUsers(user);

                skillsRepository.save(skills);
                response.setMessage("Skill created successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(skills);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }


    @Override
    public CustomResponse<?> addSoftSkill(SoftSkillsDto softSkillsDto, Long userId) {
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
                // Check if the skillsName already exists for the user
                Skills existingSkill = skillsRepository.findBySkillsNameAndUsers(softSkillsDto.getSkillsName(), user);
                if (existingSkill != null) {
                    response.setMessage("Skill with the same name already exists for the user");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }

                // Count existing soft skills for the user
                long existingSoftSkillsCount = skillsRepository.countSoftSkillsByUserId(userId);

                // Check if the user has already reached the limit of five soft skills
                if (existingSoftSkillsCount >= 5) {
                    response.setMessage("User has already reached the limit of five soft skills");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }

                Skills skills = new Skills();
                skills.setSkillsName(softSkillsDto.getSkillsName());
                skills.setSoftSkillsLevel(softSkillsDto.getSoftSkillsLevel());
                skills.setUsers(user);

                skillsRepository.save(skills);
                response.setMessage("Skill created successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(skills);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }



    @Override
    public CustomResponse<?> getLanguageSkillsByUserId(Long userId) {
        CustomResponse<List<SkillsRepository.LanguageInterface>> response = new CustomResponse<>();

        try {
            List<SkillsRepository.LanguageInterface> skillList = skillsRepository.findLanguageSkillsByUserId(userId );

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

    @Override
    public CustomResponse<?> getSoftSkillsByUserId(Long userId) {
        CustomResponse<List<SkillsRepository.SoftSkillInterface>> response = new CustomResponse<>();

        try {
            List<SkillsRepository.SoftSkillInterface> skillList = skillsRepository.findSoftSkillLevelByUserId(userId);

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

    @Override
    public CustomResponse<?> editTechnicalSkill(SkillsDto updatedSkillsDto, Long userId, Long skillId) {
        CustomResponse<Skills> response = new CustomResponse<>();

        try {

            Optional<Skills> skillOptional = skillsRepository.findByUsersAndId(userId, skillId);

            if (skillOptional.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Skills skill = skillOptional.get();
                skill.setSkillsName(updatedSkillsDto.getSkillsName());
                skill.setTechnicalLevel(updatedSkillsDto.getTechnicalLevel());

                skillsRepository.save(skill);

                response.setMessage("Skill updated successfully");
                response.setPayload(skill); // Make sure to return the updated skill in the response
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
    public CustomResponse<?> editLanguageSkill(SkillsLanguageDto updatedSkillsDto, Long userId, Long skillId) {
        CustomResponse<Skills> response = new CustomResponse<>();

        try {

            Optional<Skills> skillOptional = skillsRepository.findByUsersAndId(userId, skillId);

            if (skillOptional.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Skills skill = skillOptional.get();
                skill.setSkillsName(updatedSkillsDto.getSkillsName());
                skill.setLanguageLevel(updatedSkillsDto.getLanguageLevel());

                skillsRepository.save(skill);

                response.setMessage("Skill updated successfully");
                response.setPayload(skill); // Make sure to return the updated skill in the response
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
    public CustomResponse<?> editSoftSkill(SoftSkillsDto updatedSkillsDto, Long userId, Long skillId) {
        CustomResponse<Skills> response = new CustomResponse<>();

        try {

            Optional<Skills> skillOptional = skillsRepository.findByUsersAndId(userId, skillId);

            if (skillOptional.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Skills skill = skillOptional.get();
                skill.setSkillsName(updatedSkillsDto.getSkillsName());
                skill.setSoftSkillsLevel(updatedSkillsDto.getSoftSkillsLevel());

                skillsRepository.save(skill);

                response.setMessage("Skill updated successfully");
                response.setPayload(skill); // Make sure to return the updated skill in the response
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

}
