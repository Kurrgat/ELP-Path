package com.example.emtechelppathbackend.security.auth;

import com.example.emtechelppathbackend.security.roles.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String firstName;
    private String lastName;
   // private Long applicationOrElpId;
   private Long scholarId;
    private String userEmail;
    private String token;
    private RoleDto role;
}
