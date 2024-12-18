package com.example.emtechelppathbackend.security.user;


import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.security.roles.RoleDto;


import com.example.emtechelppathbackend.skills.Skills;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class UsersDto {
    private Long id;
    private String firstName;
    private String username;
    private String lastName;

    private String userEmail;

    private String userPassword;

    private RoleDto role;
    private String profileImage;
    private ChapterV2 chapter;


}
