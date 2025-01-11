package com.example.emtechelppathbackend.jobopportunities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobOpportunityDtoView {
    private Long id;

    private String jobTitle;

    private String jobDescription;

    private String howToApply;

    private LocalDateTime applicationDeadLine;

    private String jobType;

    private String organization;

    private List<String> jobQualifications;

    private List<String> jobResponsibilities;

    private Integer jobSalary;

    private String educationLevel;

    private String jobPoster;
}
