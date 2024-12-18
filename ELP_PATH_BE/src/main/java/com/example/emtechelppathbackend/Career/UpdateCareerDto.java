package com.example.emtechelppathbackend.Career;

import com.example.emtechelppathbackend.utils.CareerRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCareerDto {
    private String companyName;
    private String organizationSector;
    private String position;
    private CareerRole careerRole;
    private String description;
    private LocalDate start_date;
    private Boolean toDate;
}
