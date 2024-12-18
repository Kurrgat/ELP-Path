package com.example.emtechelppathbackend.Career;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.CareerRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "career")
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String organizationSector;
    private String position;
    @Enumerated(EnumType.STRING)
    @Column (name = "career_role")
    private CareerRole careerRole;
    @Length(max = 10000)
    private String description;


    private LocalDate start_date;


    private LocalDate  end_date;

    private Boolean  toDate;

    //One User can have many careers
    @JoinColumn(name = "users_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;
}
