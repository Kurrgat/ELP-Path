package com.example.emtechelppathbackend.auditing;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Data
@Table(name = "auditing")
@NoArgsConstructor
public class Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;
    private LocalDateTime loginAttempt;
//    private LocalDateTime lastActive;
    private String operatingSystem;
    private String userEmail;
    private String ipAddress;
}
