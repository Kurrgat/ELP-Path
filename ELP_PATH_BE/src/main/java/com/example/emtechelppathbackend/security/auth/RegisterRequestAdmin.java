package com.example.emtechelppathbackend.security.auth;


import com.example.emtechelppathbackend.security.roles.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestAdmin {
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPassword;
    @JsonProperty
    private Role role;

}
