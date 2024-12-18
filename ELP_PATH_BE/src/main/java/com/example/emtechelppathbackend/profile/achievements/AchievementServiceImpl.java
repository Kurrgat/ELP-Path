package com.example.emtechelppathbackend.profile.achievements;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.skills.Skills;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService{

    private final AchievementsRepository achievementsRepository;
    private final UsersRepository usersRepository;
    @Override
    public CustomResponse<Achievements> addAchievements(AchievementsDto achievementsDto, Long userId) {
        CustomResponse<Achievements> response = new CustomResponse<>();
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);

            if (userOptional.isEmpty()) {
                response.setMessage("User does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                Users user = userOptional.get();
                Optional<Achievements>optional=achievementsRepository.findByNameAndUserId(achievementsDto.getName(), user.getId());

                // Check for duplicate achievement name
                if (optional.isPresent()) {
                    response.setMessage("Achievement name already exists for this user");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    response.setPayload(null);
                    return response;
                }

                // Count the number of achievements associated with the user
                long achievementsCount = achievementsRepository.countByUser(user);

                // Check if the user already has 3 achievements
                if (achievementsCount >= 3) {
                    response.setMessage("User has reached the limit of 3 achievements");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                } else {
                    Achievements achievements = new Achievements();
                    achievements.setName(achievementsDto.getName());
                    achievements.setDescription(achievementsDto.getDescription());
                    achievements.setUser(user);

                    achievementsRepository.save(achievements);

                    response.setMessage("Achievement added successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(achievements);
                }
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
    public CustomResponse<?> getAchievementsByUserId(Long userId) {
        CustomResponse<List<Achievements>> response = new CustomResponse<>();
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);

            if (userOptional.isEmpty()) {
                response.setMessage("User does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }
            List<Achievements> userAchievements = achievementsRepository.findByUserId(userId);

            if (userAchievements.isEmpty()){
                response.setMessage("No achievements found ");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }else {
                response.setMessage("Achievements retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(userAchievements);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateAchievements(AchievementsDto achievementsDto, Long userId, Long id) {
        CustomResponse<Achievements> response = new CustomResponse<>();

        try {

            Optional<Achievements> achievementsOptional = achievementsRepository.findByUserIdAndId(userId, id);

            if (achievementsOptional.isEmpty()) {
                response.setMessage("Skill not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
              Achievements achievements = achievementsOptional.get();
                achievements.setName(achievementsDto.getName());
                achievements.setDescription(achievementsDto.getDescription());


               achievementsRepository.save(achievements);

                response.setMessage("achievements updated successfully");
                response.setPayload(achievements); // Make sure to return the updated skill in the response
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
    public CustomResponse<?> deleteAchievements( Long userId,Long id) {
        CustomResponse<Achievements> response = new CustomResponse<>();

        try {
            Optional<Achievements> achievement = achievementsRepository.findByUserIdAndId(userId,id);
            if (achievement.isEmpty()) {
                response.setMessage("Achievement not found or does not belong to the user");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
            achievementsRepository.deleteById(id);
            response.setMessage("Achievement deleted successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }


}
