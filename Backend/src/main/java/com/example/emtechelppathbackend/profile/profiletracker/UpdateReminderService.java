package com.example.emtechelppathbackend.profile.profiletracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.emtechelppathbackend.communication.sms.SMSBODY;
import com.example.emtechelppathbackend.communication.sms.SMSService;
import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.profile.ProfileResponse;
import com.example.emtechelppathbackend.profile.ProfileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateReminderService {
    private final UsersRepository usersRepository;
    private final ProfileProgressService profileProgressService;
    private final EmailService emailService;
    private final SMSService smsService;
    private final ProfileService profileService;
    
    public String profileUpdateEmailReminder(Long userId) {
        ArrayList<String> pending = new ArrayList<>();
        EmailDetails emailDetails = new EmailDetails();

        try {
            Optional<Users> optional = usersRepository.findById(userId);

            if(optional.isEmpty()) {
                return "User with Id "+userId+" does not exist";
            } else {
                Users user = optional.get();

                try {
                   CustomResponse<ProgressResponse> updateStatus = profileProgressService.getProfileUpdateStatus(userId);
                   ProgressResponse progressResponse = updateStatus.getPayload();

                   if(progressResponse.percentage != 100) {
                        if(progressResponse.bioTracker.percentageDone != 100) {
                            pending.addAll(progressResponse.bioTracker.getPending());
                        }
                        if(progressResponse.profileTracker.percentageDone != 100) {
                            pending.addAll(progressResponse.profileTracker.getPending());
                        }
                        if(progressResponse.educationTracker.percentageDone != 100) {
                            pending.addAll(progressResponse.educationTracker.getPending());
                        }
                        if(progressResponse.skillsTracker.percentageDone != 100) {
                            pending.addAll(progressResponse.skillsTracker.getPending());
                        }
                        if(progressResponse.careerTracker.percentageDone != 100) {
                            pending.addAll(progressResponse.careerTracker.getPending());
                        }

                        StringBuilder pendingUpdates = new StringBuilder();
                        for(String pendingUpdate : pending) {
                            pendingUpdates.append("• ").append(pendingUpdate).append("\n");
                        }

                        String message = "Dear "+user.getFirstName()+",\n"
                        + "We hope you're doing good. Kindly update the following profile details to ensure your data is upto date. \n\n"
                        + pendingUpdates+"\n"
                        + "Up-to date profile information enables use to present you with content that's useful to you. \n\n"
                        + "Best Regards, \n admin@elp.co.ke";

                        if(user.getUserEmail() != null && !user.getUserEmail().isEmpty()) {
                            emailDetails.setRecipient(user.getUserEmail());
                            emailDetails.setSubject("ELP PATH Profile Update Reminder");
                            emailDetails.setMessageBody(message);
                            
                            emailService.sendWithOutAttachment(emailDetails);
                        return "Update email sent out successfully";
                        } else {
                            return "User doesn't have an email";
                        }

                       
                   } else {
                        return "User profile is already up-to date";
                   }
                } catch (Exception e) {
                    log.debug("Exception is: "+e.getMessage());
                    return e.getMessage();
                }
            }
        } catch (Exception e) {
            log.debug("Error is:" +e.getMessage());
            return e.getMessage();
        }
    }

    public void sendSmsProfileUpdateReminder(Long userId) {
        ArrayList<String> pending = new ArrayList<>();
        SMSBODY smsbody = new SMSBODY();

        try {
            Optional<Users> optionalUser = usersRepository.findById(userId);

            if(optionalUser.isEmpty()) {
                System.out.println("user with id "+userId+" does not exist");
            } else {
                Users user = optionalUser.get();

                CustomResponse<ProfileResponse> profileResponse = profileService.getProfileByUserId(user.getId());
                Profile profile = profileResponse.getPayload().getProfile();

                if(profile.getPhoneNo() != null) {
                    smsbody.setPhoneNo(profile.getPhoneNo());
                }

                CustomResponse<ProgressResponse> updateStatus = profileProgressService.getProfileUpdateStatus(user.getId());
                ProgressResponse progressResponse = updateStatus.getPayload();

                if(progressResponse.percentage != 100) {
                    if(progressResponse.profileTracker.percentageDone != 100) {
                        pending.add("Profile");
                    }
                    if(progressResponse.bioTracker.percentageDone != 100) {
                        pending.add("Bio");
                    }
                    if(progressResponse.educationTracker.percentageDone != 100) {
                        pending.add("Education");
                    }
                    if(progressResponse.careerTracker.percentageDone != 100) {
                        pending.add("Career");
                    }
                    if(progressResponse.skillsTracker.percentageDone != 100) {
                        pending.add("Skills");
                    }
                }

                smsbody.setMessage("Hi "+user.getFirstName()+"Kindly update update your "+pending+" details");
                smsService.sendSms(smsbody);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

        @Scheduled(cron = "0 0 9 1 1/3 ?")
    public String sendUpdateReminderToAll() {
        System.out.println("....... Sending out profile update reminders on "+LocalDateTime.now());
        try {
            List<Users> users = usersRepository.findAll();

            if(users.isEmpty()) {
                return "No users found";
            } else {
                for(Users user:users) {
                    Long userId = user.getId();
                    profileUpdateEmailReminder(userId);
                }
                System.out.println("info: Done sending profile update emails to "+users.size()+" users on "+LocalDate.now()+" at "+LocalTime.now());
                return "update emails sent out";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @Scheduled(cron = "0 30 9 1 1/3 ?")
    public String sendingSMSReminders() {
        int success = 0;
        try {
            List<Users> users = usersRepository.findAll();

            if(users.isEmpty()) {
                return "No user found";
            } else {
                for (Users user : users) {
                    sendSmsProfileUpdateReminder(user.getId());
                    success++;
                }
                log.info("Sent out "+success+" on "+LocalDateTime.now());
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return e.getMessage();
        }
        return null;
    }
}
