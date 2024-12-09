package com.example.emtechelppathbackend.events.eventsv2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepov2 extends JpaRepository<EventsV2, Long> {
    List<EventsV2> findEventsByChapterId(Long chapterId);

    List<EventsV2> findEventsByHubId(Long hubId);

}
