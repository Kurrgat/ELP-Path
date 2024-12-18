package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.EducationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDto {
    private Long id;
    private String school_name;
    private Course userCourse;
    private Institution institution;
    private String course;
    private EducationType educationType;
    private UsersDto user;

}
