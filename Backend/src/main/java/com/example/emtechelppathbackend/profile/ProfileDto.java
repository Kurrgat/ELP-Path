package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.scholars.ScholarCategories;
import com.example.emtechelppathbackend.scholars.ScholarDTO;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileDto {
    private Long id;
    private String email;
    private String title;
    private String website;
    private String phoneNo;
    private String profileImage;
    private String currentCountryofResidence;
    private String currentCityofResidence;
    private LocalDateTime yearOfSelection;
    private JobStatus jobStatus;
    private Branch homeBranch;
    private ScholarCategories scholarCategory;
    private UsersDto usersDto;



}