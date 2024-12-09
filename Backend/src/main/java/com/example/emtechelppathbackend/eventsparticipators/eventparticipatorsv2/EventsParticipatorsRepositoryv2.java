package com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2;

import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.eventsparticipators.EventsParticipators;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsParticipatorsRepositoryv2 extends JpaRepository<EventsParticipatorsv2, Long> {
	  List<EventsParticipatorsv2> findByEventAndParticipator(EventsV2 event, Users participator);

	  List<EventsParticipatorsv2> findByEvent_Id(Long eventId);


}
