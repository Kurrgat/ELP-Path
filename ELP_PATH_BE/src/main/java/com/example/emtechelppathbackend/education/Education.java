package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.country.Country;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.CourseLevel;
import com.example.emtechelppathbackend.utils.EducationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "education")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private  Institution institution;
    @ManyToOne
    private Course course;
    @Enumerated(EnumType.STRING)
    @Column(name = "education_type")
    private EducationType educationType;
    @Enumerated(EnumType.STRING)
    @Column(name = "course_level")
    private CourseLevel courseLevel;
    private LocalDate startYear;
    private LocalDate graduationYear;
    private String grade;
    private LocalDate expectedGraduationYear;
    @JsonIgnore
    @JoinColumn(name = "users_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;


}
