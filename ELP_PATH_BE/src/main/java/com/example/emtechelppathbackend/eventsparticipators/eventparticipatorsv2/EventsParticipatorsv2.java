package com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2;

import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
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
@Table(name = "events_participatorsv2")
public class EventsParticipatorsv2 {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "event_id")
	  private EventsV2 event;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private Users participator;

	  private boolean activeParticipation;

	  private LocalDateTime bookingParticipationTime;

	  private LocalDateTime cancellingParticipationTime;



}
