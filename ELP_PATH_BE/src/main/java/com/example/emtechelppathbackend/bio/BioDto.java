package com.example.emtechelppathbackend.bio;

import com.example.emtechelppathbackend.security.user.Users;
import lombok.Data;

@Data
public class BioDto {
    private Long id;
    private String description;
    private Users user;

}
