package com.example.emtechelppathbackend.application.scholareducation;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.school.School;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ScholarEducationDto {
    private Long id;
    private Long form;
    private Long term;
    private School school;
    private LocalDate opening_date;
    private String openingGrade;
    private String midTermGrade;
    private String closingGrade;
    @JsonIgnore//Properties("hibernateLazyInitializer")
    private Application application;
}
