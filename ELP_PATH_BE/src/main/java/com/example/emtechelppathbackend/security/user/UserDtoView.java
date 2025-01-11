package com.example.emtechelppathbackend.security.user;

import lombok.Data;

@Data
public class UserDtoView {
    private Long id;
    private String userEmail;
    private String username;
    private String firstName;
    private String lastName;
}
