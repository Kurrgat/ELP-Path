package com.example.emtechelppathbackend.eventsparticipators;

import com.example.emtechelppathbackend.events.Events;
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
@Table(name = "events_participators")
public class EventsParticipators {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "event_id")
	  private Events event;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private Users participator;

	  private boolean activeParticipation;

	  private LocalDateTime bookingParticipationTime;

	  private LocalDateTime cancellingParticipationTime;
}
