package com.example.emtechelppathbackend.intern;


import com.example.emtechelppathbackend.application.wtf_application.Gender;
import com.example.emtechelppathbackend.school.School;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "intern")
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    String emailAddress;
    Gender gender;
    String email;

    @ManyToOne
    @JoinColumn(name = "high_school")
    School highSchool;
    String guardianFirstName;
    String guardianLastName;
    String relationshipToYou;
    String location;
    String phoneNo;
    String alternativePhoneNumber;
    String guardianPhoneNumber;
    boolean acceptedTermsAndConditions;
}
