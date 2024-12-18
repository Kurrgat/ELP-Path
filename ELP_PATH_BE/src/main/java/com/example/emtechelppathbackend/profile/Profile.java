package com.example.emtechelppathbackend.profile;

import java.time.LocalDateTime;

import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.scholars.ScholarCategories;
import com.example.emtechelppathbackend.security.user.Users;

import com.example.emtechelppathbackend.utils.BusinessStatus;
import com.example.emtechelppathbackend.utils.KenyaCounty;
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
    private LocalDateTime yearOfSelection;
    private String currentCountryofResidence;
    private String currentCityofResidence;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;


    private String businessName;
    @Enumerated(EnumType.STRING)
    private BusinessStatus businessStatus;
    private String businessRegNo;

   @ManyToOne
   @JoinColumn(name = "kenyan_county")
    private KenyanCounty kenyanCounty;


    @Enumerated(EnumType.STRING)
    private ScholarCategories scholarCategory;

    private String profileImage;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private Users user;


}