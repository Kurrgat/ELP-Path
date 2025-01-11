package com.example.emtechelppathbackend.school;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "school_details")
public class School {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @Column(updatable = false)
	  private LocalDateTime recordDate = LocalDateTime.now();

	  @Column(nullable = false, unique = true)
	  private String schoolName;

	  private String additionalInformation;
}
