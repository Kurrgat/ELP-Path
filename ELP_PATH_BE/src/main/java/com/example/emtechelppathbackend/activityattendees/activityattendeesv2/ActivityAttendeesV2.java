package com.example.emtechelppathbackend.activityattendees.activityattendeesv2;

import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activity_attendeesv2")
public class ActivityAttendeesV2 {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private Users attendee;

	  @ManyToOne
	  @JoinColumn(name = "activity_id")
	  private ActivityV2 activity;

	  private LocalDateTime bookingAttendanceDate;

	  private LocalDateTime cancellingAttendanceDate;

	  private boolean activeAttendance;
}
