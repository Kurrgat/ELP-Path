package com.example.emtechelppathbackend.Career;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CareerRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerDto {
    private Long id;
    private String companyName;
    private String organizationSector;
    private String position;
    private CareerRole careerRole;
    private String description;
    private LocalDate start_date;
    private LocalDate  end_date;
    private Boolean toDate;

}
