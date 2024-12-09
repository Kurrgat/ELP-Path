package com.example.emtechelppathbackend.emails;

import com.example.emtechelppathbackend.activities.Activity;
import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.attachabledata.AttachableData;
import com.example.emtechelppathbackend.events.Events;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunity;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String emailSender;

    @Autowired
    private JavaMailSender mailSender;



    @Override
    public String sendWithOutAttachment(EmailDetails emailDetails) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            //setting up necessary details
            simpleMailMessage.setFrom(emailSender);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            //sending the mail
            javaMailSender.send(simpleMailMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return "Error when sending mail";
        }
    }
    //sending mail with attachment

    @Override
    public String sendMailWithAttachment(EmailDetails emailDetails) {
        //creating a mimemessage

        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            //setting multipart as true for attachment to be sent
            mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(emailSender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            //adding the attachment
            FileSystemResource emailFile = new FileSystemResource(new File(emailDetails.getAttachment()));

            mimeMessageHelper.addAttachment(Objects.requireNonNull(emailFile.getFilename()), emailFile);

            //sending the mail
            javaMailSender.send(mimeMailMessage);
            return "attached mail sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error when sending mail with attachment";
        }
    }

    @Override
    public void sendMailWithAttachment(EmailDetails emailDetails, byte[] attachmentData, String attachmentFilename) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(emailSender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            // Attach the provided attachment data
            ByteArrayResource attachmentResource = new ByteArrayResource(attachmentData);
            mimeMessageHelper.addAttachment(attachmentFilename, attachmentResource);

            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

        @Override
    public void sendMailWithAttachmentv2(EmailDetails emailDetails, byte[] attachmentData, String attachmentFilename) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(emailSender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            // Attach the provided attachment data
            ByteArrayResource attachmentResource = new ByteArrayResource(attachmentData);
            mimeMessageHelper.addAttachment(attachmentFilename, attachmentResource);

            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    //format for the body of activity emails
    @Override
    public String generateActivityNotificationBody(Activity activity) {
        return String.format(
                "Activity Name: %s\n" +
                        "Description: %s\n" +
                        "Location: %s\n" +
                        "Date and Time: %s\n",
                activity.getActivityName(),
                activity.getActivityDescription(),
                activity.getActivityLocation(),
                activity.getActivityDate()
        );
    }

    //format for the body of activity emails
    @Override
    public String generateActivityNotificationBodyv2(ActivityV2 activity) {
        return String.format(
                "Activity Name: %s\n" +
                        "Description: %s\n" +
                        "Location: %s\n" +
                        "Date and Time: %s\n",
                activity.getActivityName(),
                activity.getActivityDescription(),
                activity.getActivityLocation(),
                activity.getActivityDate()
        );
    }

    @Override
    public String generateEventsNotificationBody(Events event) {
        return String.format(
                "Notification Type : %s\n" +
                        "Event Name : %s\n" +
                        "Description : %s\n" +
                        "Organizer : %s\n" +
                        "Location : %s\n" +
                        "Link : %s\n" +
                        "Date and Time : %s\n",
                "Event Alert",
                event.getEventName(),
                event.getEventDescription(),
                event.getOrganizer(),
                event.getLocation(),
                event.getEventLink(),
                event.getEventDate()
        );
    }

        @Override
    public String generateEventsNotificationBodyv2(EventsV2 event) {
        return String.format(
                "Notification Type : %s\n" +
                        "Event Name : %s\n" +
                        "Description : %s\n" +
                        "Organizer : %s\n" +
                        "Location : %s\n" +
                        "Link : %s\n" +
                        "Date and Time : %s\n",
                "Event Alert",
                event.getEventName(),
                event.getEventDescription(),
                event.getOrganizer(),
                event.getLocation(),
                event.getEventLink(),
                event.getEventDate()
        );
    }

    @Override
    public String generateJobOpportunityBody(JobOpportunity jobOpportunity) {

        return String.format(
                "Notification Type: %s\n" +
                        "Post Name: %s\n" +
                        "Organisation Hiring: %s\n" +
                        "Application Deadline: %s\n",
                "Job Opportunity Alert",
                jobOpportunity.getJobTitle(),
                // jobOpportunity.getOrganization().getOrganizationName(),
                jobOpportunity.getApplicationDeadLine()
        );
    }


    public EmailDetails SetRegistrationConfirmationBody(Users user) {
        EmailDetails registerNotice = new EmailDetails();

        registerNotice.setSubject("Successful Registration");
        registerNotice.setMessageBody("Welcome to ELP and WTF path, \n" + user.getFirstName() + " " + user.getLastName() + "\n Your account is successfully created");
        registerNotice.setRecipient(user.getUserEmail());
        return registerNotice;
    }

    @Override
    public EmailDetails SetResetPasswordRequest(Users user) {
        EmailDetails resetRequest = new EmailDetails();

        String resetLink = "192.168.100.239:4200/newpassword/";
        String emailContent = "Click the link to reset your password: " + resetLink + user.getPasswordResetToken();
        resetRequest.setRecipient(user.getUserEmail());
        resetRequest.setSubject("Password Reset");
        resetRequest.setMessageBody(emailContent);
        return resetRequest;
    }


    @Override
    public void sendEmailWithAttachmentToRecipients(List<Users> recipients, String subject, String body, AttachableData attachableData) {
        for (Users recipient : recipients) {
            String recipientEmail = recipient.getUserEmail();

            byte[] attachmentData = Base64.getDecoder()
                    .decode(attachableData.getData());
            String attachmentFileName = attachableData.getName();

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(recipientEmail);
            emailDetails.setSubject(subject);
            emailDetails.setMessageBody(body);

            sendMailWithAttachment(emailDetails, attachmentData, attachmentFileName);
        }
    }

        @Override
    public void sendEmailWithAttachmentToRecipientsv2(List<Users> recipients, String subject, String body, Set<String> imageUrls) {
        for (Users recipient : recipients) {
            String recipientEmail = recipient.getUserEmail();
            List<byte[]> imagesBytes = new LinkedList<>();

            for(String imgUrl: imageUrls){
                try {
                    ClassPathResource resource = new ClassPathResource("images/"+imgUrl);

                    if(resource.exists() && resource.isReadable()) {
                        InputStream inputStream = resource.getInputStream();
                        byte[] imageByte = IOUtils.toByteArray(inputStream);

                        imagesBytes.add(imageByte);
                    }
                } catch (Exception e) {
                    
                }
            }

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(recipientEmail);
            emailDetails.setSubject(subject);
            emailDetails.setMessageBody(body);

            // sendMailWithAttachmentv2(emailDetails, attachmentData, attachmentFileName);
            sendMailWithAttachmentv2(emailDetails, imagesBytes.get(0), imagesBytes.get(0).getClass().getName());
        }
    }

            @Override
    public void sendEmailWithAttachmentToRecipientsv3(List<Users> recipients, String subject, String body, String imageUrl) {
        for (Users recipient : recipients) {
            String recipientEmail = recipient.getUserEmail();

                try {
                    ClassPathResource resource = new ClassPathResource("images/"+imageUrl);

                    if(resource.exists() && resource.isReadable()) {
                        InputStream inputStream = resource.getInputStream();
                        byte[] imageByte = IOUtils.toByteArray(inputStream);

                        EmailDetails emailDetails = new EmailDetails();
                        emailDetails.setRecipient(recipientEmail);
                        emailDetails.setSubject(subject);
                        emailDetails.setMessageBody(body);

                        sendMailWithAttachmentv2(emailDetails, imageByte, imageByte.getClass().getName());
                    }
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
        }
    }




}
