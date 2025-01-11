package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.scholars.ScholarCategories;
import com.example.emtechelppathbackend.scholars.ScholarDTO;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.BusinessStatus;
import com.example.emtechelppathbackend.utils.KenyaCounty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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


    private String businessName;
    private BusinessStatus businessStatus;
    private String businessRegNo;
    private KenyanCounty kenyanCounty;

    private Branch homeBranch;
    private ScholarCategories scholarCategory;
    private UsersDto usersDto;



}