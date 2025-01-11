package com.example.emtechelppathbackend.scholars.join;

public record RegisterAsUserRequest(String email, String password, String username,String pfNumberOrScholarCode, boolean acceptedTermsAndConditions) {
}
