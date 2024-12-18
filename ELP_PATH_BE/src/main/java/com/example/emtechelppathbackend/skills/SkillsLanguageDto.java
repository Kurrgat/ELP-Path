package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.LanguageLevel;
import com.example.emtechelppathbackend.utils.TechnicalLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SkillsLanguageDto {
    private String skillsName;
    private LanguageLevel languageLevel;

    @JsonIgnore
    private Users users;
}
