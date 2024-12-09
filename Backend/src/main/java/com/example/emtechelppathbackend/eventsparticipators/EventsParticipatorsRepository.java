package com.example.emtechelppathbackend.eventsparticipators;

import com.example.emtechelppathbackend.events.Events;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventsParticipatorsRepository extends JpaRepository<EventsParticipators, Long> {
	  List<EventsParticipators> findByEventAndParticipator(Events event, Users participator);

	  List<EventsParticipators> findByEvent_Id(Long eventId);

    List<EventsParticipators> findByEvent_IdAndParticipator_Id(Long eventId, Long id);
}
