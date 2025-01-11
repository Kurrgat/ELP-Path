package com.example.emtechelppathbackend.otp;


import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.profile.ProfileService;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    @Autowired
    private final OtpRepository otpRepository;
    @Autowired
    private final JavaMailSender javaMailSender;
    @Autowired
    private final ProfileService profileService;


    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;



    @Override
    public CustomResponse<String> generateOtp(String email, String phoneNumber) {
        CustomResponse<String> response = new CustomResponse<>();
        try {
            var otp = String.format("%06d", new Random().nextInt(1000000));


            // Store the OTP in the database
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setPhoneNumber(phoneNumber);
            otpEntity.setOtp(otp);


            // Set an expiration time for the OTP (e.g., 5 minutes from now)
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
            otpEntity.setExpirationTime(expirationTime);

            // Save the OTP entity to the database
            otpRepository.save(otpEntity);


            // Send the OTP to the user via email
            sendOtpByEmail(email, otp);


            // Send the OTP to the user via SMS
            sendOtpBySms(phoneNumber, otp);
            System.out.println(otp);

            response.setMessage("OTP generated successfully and sent to your email/phone");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(otp);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    private void sendOtpByEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@emtechhouse.co.ke");
            message.setTo(email);
            message.setSubject("OTP Verification Code");
            message.setText("Your OTP code is: " + otp);
            javaMailSender.send(message);

        } catch (Exception e) {
            // Handle email sending errors
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private void sendOtpBySms(String phoneNumber, String otp) {
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your OTP is: " + otp
            ).create();
        } catch (Exception e) {
            // Handle SMS sending errors
            throw new RuntimeException("Failed to send SMS: " + e.getMessage());
        }
    }
    @Override
    public CustomResponse<?> verifyOtp(String email, String phoneNumber, String otp) {
        CustomResponse<String> response = new CustomResponse<>();
        try {
            // Check if the OTP exists for the given email or phone number
            List<OtpEntity> otpEntities = otpRepository.findByEmailOrPhoneNumber(email, phoneNumber);

            // Check if any OTP entries were found
            if (!otpEntities.isEmpty()) {
                // Assuming you want to check each OTP entry
                for (OtpEntity otpEntity : otpEntities) {
                    // Check if the OTP has not expired (e.g., within a certain time frame)
                    LocalDateTime otpExpirationTime = otpEntity.getExpirationTime();
                    LocalDateTime currentTime = LocalDateTime.now();

                    if (otpExpirationTime != null && currentTime.isBefore(otpExpirationTime)) {
                        if (otp.equals(otpEntity.getOtp())) {
                            // OTP is valid, so delete it from the database
                            otpRepository.delete(otpEntity);
                            response.setMessage("OTP verified. You can log in now.");
                            response.setStatusCode(HttpStatus.OK.value());
                            response.setSuccess(true);
                            // Assuming you want to break out of the loop after the first valid OTP is found
                            break;
                        } else {
                            response.setMessage("Invalid OTP. Please try again.");
                            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                            response.setSuccess(false);
                        }
                    } else {
                        response.setMessage("OTP has expired. Please request a new OTP.");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    }
                }
            } else {
                response.setMessage("No OTP found for this email or phone number.");
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

    public CustomResponse<?> getPhoneEmail(Long userId) {
        CustomResponse<Map<String, String>> response = new CustomResponse<>();
        Map<String, String> userData = new HashMap<>();
        var profileResponse = profileService.getProfileByUserId(userId);

        try {
            Profile profile = profileResponse.getPayload().getProfile();

            if(profile.getEmail() != null && !profile.getEmail().isEmpty()) {
                userData.put("email",profile.getEmail());
            } else {
                userData.put("email", "");
            }

            if(profile.getPhoneNo() != null && !profile.getPhoneNo().isEmpty()) {
                userData.put("phone", profile.getPhoneNo());
            } else {
                userData.put("phone","");
            }

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Successful");
            response.setPayload(userData);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

}