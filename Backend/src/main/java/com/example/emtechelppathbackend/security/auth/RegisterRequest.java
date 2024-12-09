package com.example.emtechelppathbackend.security.auth;

import com.example.emtechelppathbackend.scholars.join.RegisterRequestParameters;
import com.example.emtechelppathbackend.security.roles.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String userEmail;
    private String userPassword;
    private RegisterRequestParameters data;
    @JsonProperty
    private Role role;
}
