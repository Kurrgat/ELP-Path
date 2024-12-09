package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.security.user.UsersDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDto {
    private Long id;
    private String school_name;
    private Course userCourse;
    private Institution institution;
    private String course;
    private String yearOfStudy;
    private String semester;
    private Boolean ongoing;


    private UsersDto user;
    @NotNull(message = "start Date should not be null")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate start_date;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate  end_date;
}
