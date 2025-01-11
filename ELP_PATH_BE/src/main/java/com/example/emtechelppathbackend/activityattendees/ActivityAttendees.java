package com.example.emtechelppathbackend.activityattendees;

import com.example.emtechelppathbackend.activities.Activity;
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
@Table(name = "activity_attendees")
public class ActivityAttendees {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private Users attendee;

	  @ManyToOne
	  @JoinColumn(name = "activity_id")
	  private Activity activity;

	  private LocalDateTime bookingAttendanceDate;

	  private LocalDateTime cancellingAttendanceDate;

	  private boolean activeAttendance;
}
