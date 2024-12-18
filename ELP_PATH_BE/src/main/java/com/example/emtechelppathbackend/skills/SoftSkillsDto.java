package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.SoftSkillsLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SoftSkillsDto {
    private String skillsName;
    private SoftSkillsLevel softSkillsLevel;

    @JsonIgnore
    private Users users;

}
