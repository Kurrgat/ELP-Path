package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.TechnicalLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SkillsDto {
    
    private String skillsName;
    private TechnicalLevel technicalLevel;

   @JsonIgnore
    private Users users;
}
