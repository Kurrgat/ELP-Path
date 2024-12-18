package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.scholars.ScholarDTO;
import com.example.emtechelppathbackend.security.user.UserDtoView;
import com.example.emtechelppathbackend.security.user.Users;
import lombok.Data;

@Data
public class ProfileDto1 {
    private String email;
    private String title;
    private String website;
    private String profileImage;
    private JobStatus jobStatus;
    private Branch homeBranch;
    private UserDtoView users;


}
