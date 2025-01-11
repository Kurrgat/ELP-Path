package com.example.emtechelppathbackend.scholarcv;

import com.example.emtechelppathbackend.Career.CareerCvDto;
import com.example.emtechelppathbackend.bio.BioCvDto;
import com.example.emtechelppathbackend.education.EducationCvDto;
import com.example.emtechelppathbackend.profile.ProfileDto;
import com.example.emtechelppathbackend.profile.achievements.AchievementsDto;
import com.example.emtechelppathbackend.skills.SkillsDto;
import com.example.emtechelppathbackend.skills.SkillsLanguageDto;
import com.example.emtechelppathbackend.skills.SoftSkillsDto;
import lombok.Data;

import java.util.List;

@Data
public class ScholarCvDto {
  private List<EducationCvDto> education;
  private List<CareerCvDto>career;
  private ProfileDto profile;
  private List<AchievementsDto>achievements;
  private BioCvDto bio;
  private List<SkillsDto>skills;
  private List<SoftSkillsDto>softSkills;
  private List<SkillsLanguageDto>languageSkills;


}
