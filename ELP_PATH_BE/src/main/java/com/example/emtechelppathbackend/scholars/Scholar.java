package com.example.emtechelppathbackend.scholars;

import com.example.emtechelppathbackend.school.School;
import com.example.emtechelppathbackend.application.wtf_application.Gender;
import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.country.Country;
import com.example.emtechelppathbackend.education.Course;
import com.example.emtechelppathbackend.education.Institution;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scholar")
public class Scholar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime recordDate = LocalDateTime.now();

    @Column(unique = true)
    private String scholarCode;

    @Column(unique = true)
    private String pfNumber;

    private String scholarFirstName;

    private String scholarLastName;

    @NotNull(message = "You must include the date of birth of the applicant")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scholarDOB;

    @NotNull(message = "You must include the gender of the applicant")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "scholar_branch")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private ScholarCategories scholarCategory;

    //when did they join the specific scholar program
    @JsonFormat(pattern = "yyyy")
    private LocalDate yearOfJoiningHighSchoolProgram;

    //when did they join the specific scholar program
    @JsonFormat(pattern = "yyyy")
    private LocalDate yearOfJoiningTertiaryProgram;

    //high school
    @ManyToOne
    @JoinColumn(name = "scholar_high_school")
    private School school;

    //tertiary institution
    @ManyToOne
    @JoinColumn(name = "scholar_tertiary_institution")
    private Institution institution;

    private String donor;

    //@NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private KenyanCounty homeCounty;

    //local or global scholar (at university level)
    @Enumerated(EnumType.STRING)
    private ScholarType scholarType;


    @ManyToOne(cascade = CascadeType.ALL)
    private Country countryOfOrigin;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "scholar_course",
            joinColumns = @JoinColumn(name = "scholar_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
    private long registeredUsers;
    private long unregisteredUsers;
}

