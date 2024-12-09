package com.example.emtechelppathbackend.security.auth;

import lombok.Data;

@Data
public class ScholarAuthDto {
    private String scholarPfNo;
    private String scholarEmail;
    private String scholarPassword;
}
