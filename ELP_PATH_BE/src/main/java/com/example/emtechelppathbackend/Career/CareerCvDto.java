package com.example.emtechelppathbackend.Career;

import com.example.emtechelppathbackend.utils.CareerRole;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CareerCvDto {
    private String companyName;
    private String position;
    private String description;
    private LocalDate start_date;
    private LocalDate  end_date;
    private Boolean toDate;
}
