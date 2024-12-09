package com.example.emtechelppathbackend.Career;
import com.example.emtechelppathbackend.security.user.UsersDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerDto {
    private Long id;
    private String companyName;
    private String title;
    private String description;
    private LocalDate start_date;
    private LocalDate  end_date;
    private UsersDto user;
}
