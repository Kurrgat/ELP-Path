package com.example.emtechelppathbackend.otp;

import com.example.emtechelppathbackend.utils.CustomResponse;

public interface OtpService {
    CustomResponse<?> generateOtp(String email, String phoneNumber);
    CustomResponse<?> verifyOtp(String email ,String phoneNumber, String otp);

    CustomResponse<?> getPhoneEmail(Long userId);
}
