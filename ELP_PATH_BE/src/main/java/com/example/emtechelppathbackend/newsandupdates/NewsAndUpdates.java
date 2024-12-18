package com.example.emtechelppathbackend.newsandupdates;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class NewsAndUpdates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Length(max = 10000)
    private String message;
    private Integer viewCount;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime date;
    private String image;

    @PrePersist
    private void onCreate() {
        date = LocalDateTime.now();
    }

}
