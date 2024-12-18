package com.example.emtechelppathbackend.communication.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;


    public SMS sendMessage(String phoneNumber, String message) {
        Twilio.init(accountSid, authToken);
        Message sendMessage = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber), message).create();
        System.out.println(sendMessage.getSid());

        SMS sms = new SMS();
        sms.setMessage(sendMessage.getBody());
//        sms.setSentOn(sendMessage.getDateSent().toLocalDateTime());
        sms.setStatus(String.valueOf(sendMessage.getStatus()));
        return sms;
    }
}
