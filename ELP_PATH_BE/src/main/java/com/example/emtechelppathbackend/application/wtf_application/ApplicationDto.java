package com.example.emtechelppathbackend.application.wtf_application;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.scholars.ScholarType;
import com.example.emtechelppathbackend.school.School;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.KenyaCounty;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationDto {
    private long id;
    private String applicantFirstName;
    private String applicantLastName;
    private Branch branch;
    private LocalDate dateOfApplication;
    private LocalDate dateOfAwarding;
    private LocalDate dateOfInterview;
    private ApplicationStatus applicationStatus;
    private Gender applicantGender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applicantDOB;
    private School school;
    private UsersDto user;
    private String scholarCode;
    @Enumerated(EnumType.STRING)
    private KenyaCounty location;
    private ScholarType scholarType;
    private String countryofOrigin;
    private String currentCountryofResidence;
    private String currentCityofResidence;
//  private InterviewReportPDF interviewReportPDF;
}
