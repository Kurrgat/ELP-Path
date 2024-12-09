package com.example.emtechelppathbackend.communication.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sms_records")
public class SMS {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String phoneNo;
    private String message;
    private String receiver;
    private LocalDateTime sentOn;
    private String status;
}
