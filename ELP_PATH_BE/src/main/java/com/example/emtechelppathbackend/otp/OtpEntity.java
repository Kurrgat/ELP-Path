package com.example.emtechelppathbackend.otp;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data

public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String email;
    private String otp;
    private  String phoneNumber;
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;



}
