package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.security.user.UsersDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddEducationDTO {

    @NotNull(message = "start Date should not be null")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate  endDate;

    private String yearOfStudy;
    private String semester;
    private Boolean ongoing;

}
