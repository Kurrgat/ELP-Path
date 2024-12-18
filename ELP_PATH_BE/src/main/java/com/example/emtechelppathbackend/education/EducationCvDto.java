package com.example.emtechelppathbackend.education;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationCvDto {
    private String institutionName;
    private String course_name;
    private LocalDate startYear;
    private LocalDate graduationYear;
}
