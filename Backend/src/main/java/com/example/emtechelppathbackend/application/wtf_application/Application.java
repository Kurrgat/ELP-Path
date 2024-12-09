package com.example.emtechelppathbackend.application.wtf_application;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.scholars.ScholarType;
import com.example.emtechelppathbackend.school.School;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.KenyaCounty;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime recordDate = LocalDateTime.now();

    private String applicantFirstName;

    private String applicantLastName;

    @NotNull(message = "You must include the date of birth of the applicant")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applicantDOB;

	@Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @NotNull(message = "You must include the gender of the applicant")
	@Enumerated(EnumType.STRING)
	private Gender applicantGender;

    @NotNull(message = "You must include the date the application was made")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfApplication;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfInterview;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfAwarding;

    @Column(unique = true)
    private String scholarCode;

    @ManyToOne
    @JoinColumn(name = "scholar_branch")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "scholar_school")
    private School school;
/*
    @OneToMany(mappedBy = "application")
    @JsonBackReference //breaking the circular reference
    private Set<SchoolHistory> schoolHistories = new HashSet<>();
*/
    @OneToOne(cascade = CascadeType.ALL)
    private Users user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private KenyaCounty location;

    //local or global scholar (at university level)
    private ScholarType scholarType;

    private String countryofOrigin;
    private String currentCountryofResidence;
    private String currentCityofResidence;
}

