package com.example.emtechelppathbackend.activitytypes.activitytypesv2;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Activity_typesv2")
public class ActivityTypeV2 {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private  Long id;

	  @Column(updatable = false)
	  private LocalDateTime recordDate = LocalDateTime.now();

	  @NotNull(message = "you must include a name for the activity type")
	  @Column(unique = true)
	  private String typeName;

	  @NotNull(message = "you must include a brief description for the activity type")
	  @Column(unique = true)
	  private String typeDescription;
}
