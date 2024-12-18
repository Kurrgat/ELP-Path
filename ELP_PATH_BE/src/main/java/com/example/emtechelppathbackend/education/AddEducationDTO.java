package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.utils.CourseLevel;
import com.example.emtechelppathbackend.utils.EducationType;

import lombok.Data;

import java.time.LocalDate;


@Data
public class AddEducationDTO {


    private CourseLevel courseLevel;
    private EducationType educationType;
    private LocalDate startYear;
    private LocalDate graduationYear;
    private String grade;
    private LocalDate expectedGraduationYear;

}
