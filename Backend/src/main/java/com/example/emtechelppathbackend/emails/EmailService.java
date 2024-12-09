package com.example.emtechelppathbackend.emails;

import com.example.emtechelppathbackend.activities.Activity;
import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.attachabledata.AttachableData;
import com.example.emtechelppathbackend.events.Events;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunity;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.CustomResponse;

import java.util.List;
import java.util.Set;

public interface EmailService {
    String sendWithOutAttachment(EmailDetails emailDetails);

    String sendMailWithAttachment(EmailDetails emailDetails);

    void sendMailWithAttachment(EmailDetails emailDetails, byte[] attachmentData, String attachmentFilename);

    void sendMailWithAttachmentv2(EmailDetails emailDetails, byte[] attachmentData, String attachmentFilename);

    String generateActivityNotificationBody(Activity activity);

    String generateActivityNotificationBodyv2(ActivityV2 activity);

    String generateEventsNotificationBody(Events event);

    String generateEventsNotificationBodyv2(EventsV2 event);

    String generateJobOpportunityBody(JobOpportunity jobOpportunity);

    EmailDetails SetRegistrationConfirmationBody(Users user);

    EmailDetails SetResetPasswordRequest(Users user);

    void sendEmailWithAttachmentToRecipients(List<Users> recipients, String subject, String body, AttachableData attachableData);

    void sendEmailWithAttachmentToRecipientsv2(List<Users> recipients, String subject, String body, Set<String> imageUrls);

    void sendEmailWithAttachmentToRecipientsv3(List<Users> recipients, String subject, String body, String imageUrl);



}
