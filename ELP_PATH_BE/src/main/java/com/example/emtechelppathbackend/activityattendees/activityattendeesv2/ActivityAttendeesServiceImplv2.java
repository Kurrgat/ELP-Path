package com.example.emtechelppathbackend.activityattendees.activityattendeesv2;

import com.example.emtechelppathbackend.activities.activitiesv2.ActivityRepositoryv2;
import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberRepositoryv2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberServicev2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberServicev2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActivityAttendeesServiceImplv2 implements ActivityAttendeesServicev2 {


    private final ActivityRepositoryv2 activityRepository;
    private final UsersRepository userRepository;
    private final ChapterMemberRepositoryv2 chapterMemberRepository;
    private final HubMemberRepov2 hubMemberRepo;
    private final ChapterMemberServicev2 chapterMemberService;
    private final HubMemberServicev2 hubMemberService;
    private final ActivityAttendeesRepositoryv2 activityAttendeesRepository;
    private final EmailService emailService;

    //attending an activity
    @Override
    @Transactional
    public CustomResponse<?> attendActivity(Long userId, Long activityId) throws NoResourceFoundException {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUser = userRepository.findById(userId);
            
            if(optionalUser.isEmpty()) {
                response.setMessage("User with ID " + userId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Optional<ActivityV2> optionalActivity = activityRepository.findById(activityId);
                if(optionalActivity.isEmpty()) {
                    response.setMessage("Activity with ID " + activityId + " not found");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {   
                    ActivityV2 activity = optionalActivity.get(); 
                    Users user = optionalUser.get();
                    if (activity.getActivityDate().isBefore(LocalDateTime.now())) {
                        response.setMessage("Cannot attend past activities");
                        response.setStatusCode(HttpStatus.FORBIDDEN.value());
                        response.setSuccess(false);
                    } else {           
                        var existingActivityAttendances = activityAttendeesRepository.findByActivityAndAttendee(activity, user);

                        ActivityAttendeesV2 existingActivityAttendance = getActivityAttendees(existingActivityAttendances);

                        if ((existingActivityAttendance != null)) {
                            response.setMessage("User is already an attendee of this activity");
                            response.setStatusCode(HttpStatus.FOUND.value());
                            response.setSuccess(false);
                        }

                        ActivityAttendeesV2 activityAttendees = new ActivityAttendeesV2();

                        activityAttendees.setActivity(activity);
                        activityAttendees.setAttendee(user);
                        activityAttendees.setBookingAttendanceDate(LocalDateTime.now());
                        activityAttendees.setActiveAttendance(true);

                        activityAttendeesRepository.save(activityAttendees);

                        activityRepository.save(activity);

                        response.setMessage("User with id"+userId+" has been added to activity with id "+activityId);
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setSuccess(true);
                        response.setPayload("added to activity");
                    }
                }
                
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    // cancelling a scheduled activity attendance
    @Override
    @Transactional
    public CustomResponse<?> cancelAttendance(Long userId, Long activityId) {
        CustomResponse<String> response = new CustomResponse<>();

        try { 
            Optional<Users> optionalUser = userRepository.findById(userId);
            
            if(optionalUser.isEmpty()) {
                response.setMessage("User with ID " + userId + " not found.");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Optional<ActivityV2> optionalActivity = activityRepository.findById(activityId);

                if(optionalActivity.isEmpty()) {
                    response.setMessage("Activity with ID " + activityId + " not found.");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    ActivityV2 activity = optionalActivity.get();
                    Users user = optionalUser.get();

                    if (activity.getActivityDate().isBefore(LocalDateTime.now())) {
                        response.setMessage("Cannot cancel attendance for past activities.");
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setSuccess(false);
                    } else {
                        Set<ActivityAttendeesV2> existingActivityAttendees = activityAttendeesRepository.findByActivity_Id(activityId);

                        Optional<ActivityAttendeesV2> foundAttendance = existingActivityAttendees.stream()
                                .filter(attendance -> attendance.getAttendee().equals(user)
                                        && (attendance.isActiveAttendance())).findFirst();

                        if (foundAttendance.isEmpty()) {
                            response.setMessage("user of email " + user.getUserEmail() + " is not attending " + activity.getActivityName() + " activity.");
                            response.setStatusCode(HttpStatus.NOT_FOUND.value());
                            response.setSuccess(false);
                        }

                        ActivityAttendeesV2 attendanceToUpdate = foundAttendance.get();
                        attendanceToUpdate.setCancellingAttendanceDate(LocalDateTime.now());
                        attendanceToUpdate.setActiveAttendance(false);

                        activityAttendeesRepository.save(attendanceToUpdate);
                        activityRepository.save(activity);

                        response.setMessage("User removed as an attendee for "+activity.getActivityName()+" activity");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setPayload("Attendance cancelled on "+LocalDateTime.now().toLocalDate());
                    }
                }
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<?> notifyChapterMembersAboutANewActivity(ActivityV2 activity) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            List<ChapterMemberV2> recipientsMembers = chapterMemberRepository.findByChapterIdAndActiveMembershipIsTrue(activity.getChapter().getId());
            List<Users> recipientSet = chapterMemberService.extractUsersFromChapterMembers(recipientsMembers);
            List<Users> recipients = new ArrayList<>(recipientSet);
            String subject = activity.getActivityName();
            String body = emailService.generateActivityNotificationBodyv2(activity);

            emailService.sendEmailWithAttachmentToRecipientsv3(recipients, subject, body, activity.getActivityImage());

            response.setMessage("Notification sent out");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(body);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> notifyHubMembersAboutANewActivity(ActivityV2 activity) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            List<HubMemberv2> recipientsMembers = hubMemberRepo.findByHubIdAndActiveMembershipIsTrue(activity.getHub().getId());
            List<Users> recipientSet = hubMemberService.extractUsersFromHubMembers(recipientsMembers);
            List<Users> recipients = new ArrayList<>(recipientSet);
            String subject = activity.getActivityName();
            String body = emailService.generateActivityNotificationBodyv2(activity);

        emailService.sendEmailWithAttachmentToRecipientsv3(recipients, subject, body, activity.getActivityImage());

        response.setMessage("Notification sent out to hub members");
        response.setStatusCode(HttpStatus.OK.value());
        response.setPayload(body);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public ActivityAttendeesV2 getActivityAttendees(List<ActivityAttendeesV2> attendances) {
        for (ActivityAttendeesV2 attendance : attendances) {
            if (attendance.isActiveAttendance()) {
                return attendance;
            }
        }
        return null;
    }
}
