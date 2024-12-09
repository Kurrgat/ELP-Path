package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.scholars.ScholarCategories;
import com.example.emtechelppathbackend.security.user.Users;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileDtoUpdate {
    private String email;
    private String title;
    private String website;
    private String phoneNo;
    private String currentCountryofResidence;
    private String currentCityofResidence;
    private LocalDateTime yearOfSelection;
    private Branch homeBranch;
    private JobStatus jobStatus;
    private ScholarCategories scholarCategory;
    private Users user;
}