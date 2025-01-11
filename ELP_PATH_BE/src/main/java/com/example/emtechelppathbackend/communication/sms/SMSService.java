package com.example.emtechelppathbackend.communication.sms;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface SMSService {
    CustomResponse<?> sendSms(SMSBODY smsbody);
}
