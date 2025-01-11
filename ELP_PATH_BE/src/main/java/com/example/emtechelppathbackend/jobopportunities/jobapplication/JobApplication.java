package com.example.emtechelppathbackend.jobopportunities.jobapplication;

import com.example.emtechelppathbackend.jobopportunities.JobOpportunity;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "job_application")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate applicationDate;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne()
    @JoinColumn(name = "opportunity_id")

    private JobOpportunity jobOpportunity;

}
