package com.example.emtechelppathbackend.communication.sms;

import lombok.Data;

@Data
public class SMSBODY {
    private String phoneNo;
    private String message;
}
