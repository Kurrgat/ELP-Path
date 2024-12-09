package com.example.emtechelppathbackend.scholars;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.country.Country;
import com.example.emtechelppathbackend.education.Institution;
import com.example.emtechelppathbackend.school.School;
import com.example.emtechelppathbackend.application.wtf_application.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScholarDTO {
    private long id;
    private String pfNumber;
    private String scholarCode;
    private String scholarFirstName;
    private String scholarLastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scholarDOB;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Branch branch;
    @Enumerated(EnumType.STRING)
    private ScholarCategories scholarCategory;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate yearOfJoiningHighSchoolProgram;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate yearOfJoiningTertiaryProgram;
    private School school;
    private Institution institution;
    private String donor;
    @Enumerated(EnumType.STRING)
    private KenyanCounty homeCounty;
    @Enumerated(EnumType.STRING)
    private ScholarType scholarType;
    private Country countryOfOrigin;
}
