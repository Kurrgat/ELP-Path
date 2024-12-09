package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SkillsDto {
    
    private String skillsName;
    private int level;
   @JsonIgnore
    private Users users;
}
