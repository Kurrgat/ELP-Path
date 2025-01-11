package com.example.emtechelppathbackend.feedback;

import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String message;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @PrePersist
    private void onCreate() {
        date = LocalDateTime.now();
    }
}
