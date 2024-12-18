package com.example.emtechelppathbackend.reward.userpoints;

import com.example.emtechelppathbackend.activities.activitiesv2.ActivityRepositoryv2;

import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.eventsparticipators.EventsParticipatorsService;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsRepositoryv2;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsv2;
import com.example.emtechelppathbackend.reward.RewardEntity;
import com.example.emtechelppathbackend.reward.RewardRepo;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPointsServiceImp implements UserPointsService{
    @Autowired
    private final UserPointsRepo userPointsRepo;
    @Autowired
    private  final UsersRepository usersRepo;
    @Autowired
    private  final RewardRepo rewardRepository;

    @Autowired
    private final EventsParticipatorsService eventsParticipatorsService;
    @Autowired
 private final EventsParticipatorsRepositoryv2 eventsParticipatorsRepository;
    @Autowired
    private final ActivityRepositoryv2 activityRepositoryv2;
    @Override
    public CustomResponse<?> redeemPoints(Long userId,  Long rewardId) {
        CustomResponse<UserPoints> response = new CustomResponse<>();
        try {
            UserPoints userPoints = userPointsRepo.findByUserId(userId)
                    .orElseThrow(() -> new Exception("User points not found for user with ID: " + userId));

            RewardEntity reward = rewardRepository.findById(rewardId)
                    .orElseThrow(() -> new Exception("Reward not found with ID: " + rewardId));

            int pointsRequired = reward.getPointsRequired();

            if (userPoints.getPoints() >= pointsRequired) {
                // Deduct points
                userPoints.setPoints(userPoints.getPoints() - pointsRequired);
                userPointsRepo.save(userPoints);



                response.setMessage("Reward redeemed successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(userPoints);
            } else {
                response.setMessage("Insufficient points to redeem the reward");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Failed to redeem reward: " + e.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> awardPoints(Long userId,  Long eventId, Long activityId) {
        CustomResponse<UserPoints> response = new CustomResponse<>();
        try {
            Users user = usersRepo.findById(userId)
                    .orElseThrow(() -> new Exception("User not found with ID: " + userId));

            UserPoints userPoints = userPointsRepo.findByUserId(userId)
                    .orElseGet(() -> createUserPoints(user)); // Pass the user to createUserPoints

            // Additional logic to award points for event participation
            awardPointsForEventParticipation( userPoints, eventId);
            awardPointsForActivityParticipation( userPoints, activityId);

            // Add the pointsToAward for other scenarios if needed

            // Save the UserPoints entity
            UserPoints awardedUserPoints = userPointsRepo.save(userPoints);

            response.setMessage("Points awarded successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(awardedUserPoints);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    private void awardPointsForEventParticipation( UserPoints userPoints, Long participatorId) {
        try {
            // Query the database for events the user participated in
            Optional<EventsParticipatorsv2> eventParticipation = eventsParticipatorsRepository.findById(participatorId);

            // Check if the event participation is present
            if (eventParticipation.isPresent()) {
                EventsParticipatorsv2 eventParticipationEntity = eventParticipation.get();
                System.out.println("Event that has been participated in: " + eventParticipationEntity);

                // Award points based on active event participation
                int totalPointsForEventParticipation = calculatePointsForEventParticipation(eventParticipationEntity);

                // Award points only if there are active participations
                if (totalPointsForEventParticipation > 0) {
                    userPoints.setPoints(userPoints.getPoints() + totalPointsForEventParticipation);
                }
            } else {
                System.out.println("Event participation with ID " + participatorId + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Assuming calculatePointsForEventParticipation now takes an EventsParticipators entity
    private int calculatePointsForEventParticipation(EventsParticipatorsv2 eventParticipation) {
        // Your logic to calculate points based on the event participation
        // Replace this with your actual logic
        return 10; // Example: Awarding 10 points for participation
    }



    private void awardPointsForActivityParticipation(UserPoints userPoints, Long participatorId) {
        try {
            // Query the database for activities the user participated in
            Optional<ActivityV2> activityParticipation = activityRepositoryv2.findById(participatorId);

            // Check if the activity participation is present
            if (activityParticipation.isPresent()) {
                ActivityV2  activityV2 = activityParticipation.get();
                System.out.println("Activity that has been participated in: " + activityV2);

                // Award points based on activity participation
                int totalPointsForActivityParticipation = calculatePointsForActivityParticipation(activityV2);

                // Award points only if there are active participations
                if (totalPointsForActivityParticipation > 0) {
                    userPoints.setPoints(userPoints.getPoints() + totalPointsForActivityParticipation);
                }
            } else {
                System.out.println("Activity participation with ID " + participatorId + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Assuming calculatePointsForActivityParticipation now takes an EventsParticipatorsv2 entity
    private int calculatePointsForActivityParticipation(ActivityV2 activityParticipation) {
        // Your logic to calculate points based on the activity participation
        // Replace this with your actual logic
        return 10; // Example: Awarding 10 points for activity participation
    }

    // Helper method to create UserPoints if not found
    private UserPoints createUserPoints(Users user) {
        UserPoints userPoints = new UserPoints();
        userPoints.setUser(user);
        userPoints.setPoints(0); // Initialize points to 0
        return userPointsRepo.save(userPoints);
    }


}
