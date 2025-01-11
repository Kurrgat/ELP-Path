package com.example.emtechelppathbackend.communication.sms;

import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.PhoneNoFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {
    private final TwilioService twilioService;
    @Override
    public CustomResponse<?> sendSms(SMSBODY smsbody) {
        CustomResponse<SMS> response = new CustomResponse<>();

        try {
            var result = twilioService.sendMessage(PhoneNoFormatter.formatPhoneNo(smsbody.getPhoneNo()), smsbody.getMessage());

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successful");
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return  response;
    }
}
