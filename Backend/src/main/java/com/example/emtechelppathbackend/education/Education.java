package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

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
    private String yearOfStudy;
    private String semester;
    private Boolean ongoing;



    @NotNull(message = "start Date should not be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true)
    private LocalDate end_date;

    //a user might have attended many schools
    @JsonIgnore
    @JoinColumn(name = "users_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

}
