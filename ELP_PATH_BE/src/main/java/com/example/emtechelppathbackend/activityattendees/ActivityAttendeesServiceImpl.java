package com.example.emtechelppathbackend.activityattendees;

import com.example.emtechelppathbackend.activities.Activity;
import com.example.emtechelppathbackend.activities.ActivityRepository;
import com.example.emtechelppathbackend.attachabledata.ImageAttachableData;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMember;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMemberRepository;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMemberService;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubmembers.HubMember;
import com.example.emtechelppathbackend.hubmembers.HubMemberRepo;
import com.example.emtechelppathbackend.hubmembers.HubMemberService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActivityAttendeesServiceImpl implements ActivityAttendeesService {


    private final ActivityRepository activityRepository;
    private final UsersRepository userRepository;
    private final ChapterMemberRepository chapterMemberRepository;
    private final HubMemberRepo hubMemberRepo;
    private final ChapterMemberService chapterMemberService;
    private final HubMemberService hubMemberService;
    private final ActivityAttendeesRepository activityAttendeesRepository;
    private final EmailService emailService;

    //attending an activity
    @Override
    @Transactional
    public void attendActivity(Long userId, Long activityId) throws NoResourceFoundException {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found"));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NoResourceFoundException("Activity with ID " + activityId + " not found"));

        if (activity.getActivityDate().isBefore(LocalDateTime.now())) {
            throw new NoResourceFoundException("Cannot attend past activities.");
        }

        var existingActivityAttendances = activityAttendeesRepository.findByActivityAndAttendee(activity, user);

		ActivityAttendees existingActivityAttendance = getActivityAttendees(existingActivityAttendances);

        if ((existingActivityAttendance != null)) {
            throw new NoResourceFoundException("User is already an attendee of this activity");
        }

        ActivityAttendees activityAttendees = new ActivityAttendees();

        activityAttendees.setActivity(activity);
        activityAttendees.setAttendee(user);
        activityAttendees.setBookingAttendanceDate(LocalDateTime.now());
        activityAttendees.setActiveAttendance(true);

        activityAttendeesRepository.save(activityAttendees);

        activityRepository.save(activity);
    }

    // cancelling a scheduled activity attendance
    @Override
    @Transactional
    public void cancelAttendance(Long userId, Long activityId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found."));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NoResourceFoundException("Activity with ID " + activityId + " not found."));

        if (activity.getActivityDate().isBefore(LocalDateTime.now())) {
            throw new NoResourceFoundException("Cannot cancel attendance for past activities.");
        }

        Set<ActivityAttendees> existingActivityAttendees = activityAttendeesRepository.findByActivity_Id(activityId);

        Optional<ActivityAttendees> foundAttendance = existingActivityAttendees.stream()
                .filter(attendance -> attendance.getAttendee().equals(user)
                        && (attendance.isActiveAttendance())).findFirst();

        if (foundAttendance.isEmpty()) {
            throw new NoResourceFoundException("user of email " + user.getUserEmail() + " is not attending " + activity.getActivityName() + " activity.");
        }

        ActivityAttendees attendanceToUpdate = foundAttendance.get();
        attendanceToUpdate.setCancellingAttendanceDate(LocalDateTime.now());
        attendanceToUpdate.setActiveAttendance(false);

        activityAttendeesRepository.save(attendanceToUpdate);
        activityRepository.save(activity);
    }


    @Override
    public void notifyChapterMembersAboutANewActivity(Activity activity) {

        Set<ChapterMember> recipientsMembers = chapterMemberRepository.findByChapter_IdAndActiveMembershipIsTrue(activity.getChapter().getId());
        Set<Users> recipientSet = chapterMemberService.extractUsersFromChapterMembers(recipientsMembers);
        List<Users> recipients = new ArrayList<>(recipientSet);
        String subject = activity.getActivityName();
        String body = emailService.generateActivityNotificationBody(activity);
        ImageAttachableData activityAttachableData = new ImageAttachableData(activity.getActivityImage());

        emailService.sendEmailWithAttachmentToRecipients(recipients, subject, body, activityAttachableData);
    }

    @Override
    public void notifyHubMembersAboutANewActivity(Activity activity) {

        Set<HubMember> recipientsMembers = hubMemberRepo.findByHub_IdAndActiveMembershipIsTrue(activity.getHub().getId());
        Set<Users> recipientSet = hubMemberService.extractUsersFromHubMembers(recipientsMembers);
        List<Users> recipients = new ArrayList<>(recipientSet);
        String subject = activity.getActivityName();
        String body = emailService.generateActivityNotificationBody(activity);
        ImageAttachableData activityAttachableData = new ImageAttachableData(activity.getActivityImage());

        emailService.sendEmailWithAttachmentToRecipients(recipients, subject, body, activityAttachableData);
    }

    @Override
    public ActivityAttendees getActivityAttendees(List<ActivityAttendees> attendances) {
        for (ActivityAttendees attendance : attendances) {
            if (attendance.isActiveAttendance()) {
                return attendance;
            }
        }
        return null;
    }
}
