package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarDTO;
import com.example.emtechelppathbackend.security.user.UsersDto;
import lombok.Data;

import java.util.List;

@Data
public class ProfileResponse {
    private Profile profile;
    private UsersDto usersDto;
    private ScholarDTO scholarDTO;

    public void setProfiles(List<Profile> modifiedProfiles) {
    }
}