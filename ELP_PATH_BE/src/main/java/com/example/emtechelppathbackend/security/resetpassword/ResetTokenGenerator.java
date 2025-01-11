package com.example.emtechelppathbackend.security.resetpassword;

import java.util.UUID;

public class ResetTokenGenerator {
    public static String generateResetToken(){
        return UUID.randomUUID().toString();
    }
}
