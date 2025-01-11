package com.example.emtechelppathbackend.jobopportunities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_opportunities")
public class JobOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A job post must have a name")
    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    @Lob
    @Length(max = 10000)
    private String jobDescription;

    private String howToApply;

    @NotNull(message = "You must specify the day after which applications will not be accepted")
    private LocalDateTime applicationDeadLine;

    @Column(updatable = false)
    private LocalDateTime recordTime = LocalDateTime.now();


    private String jobType;

    private String organization;

    @ElementCollection
    @CollectionTable(name = "qualifications")
    private List<String> jobQualifications = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "responsibilities")
    private List<String> jobResponsibilities = new ArrayList<>();

    private Integer jobSalary;

    private String educationLevel;

    private String jobPoster;
}
