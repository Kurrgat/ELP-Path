package com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2;

import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.eventsparticipators.EventsParticipators;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsParticipatorsRepositoryv2 extends JpaRepository<EventsParticipatorsv2, Long> {
	  List<EventsParticipatorsv2> findByEventAndParticipator(EventsV2 event, Users participator);

	  List<EventsParticipatorsv2> findByEvent_Id(Long eventId);

@Query(value = "SELECT * FROM events_participatorsv2 WHERE  user_id = :userId",nativeQuery = true)
List<EventsParticipatorsv2> findByUserId(Long userId);

@Query(value = "SELECT * FROM events_participatorsv2 WHERE event_id = :eventId ",nativeQuery = true)
List<EventsParticipatorsv2> findByEventId(Long eventId);


}
