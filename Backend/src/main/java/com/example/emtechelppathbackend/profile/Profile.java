package com.example.emtechelppathbackend.profile;

import java.time.LocalDateTime;

import com.example.emtechelppathbackend.scholars.ScholarCategories;
import com.example.emtechelppathbackend.security.user.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String title;
    private String website;
    private String phoneNo;
    private String phoneNo2;
    private String phoneNo3;



    private LocalDateTime yearOfSelection;

    //country and city
    private String currentCountryofResidence;
    private String currentCityofResidence;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @Enumerated(EnumType.STRING)
    private ScholarCategories scholarCategory;

    private String profileImage;

    // a user can have only one profile
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private Users user;

}