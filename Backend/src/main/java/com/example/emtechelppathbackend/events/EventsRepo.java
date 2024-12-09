package com.example.emtechelppathbackend.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepo extends JpaRepository<Events, Long> {
    List<Events> findEventsByChapterId(Long chapterId);

    List<Events> findEventsByHubId(Long hubId);

    @Override
    @Query(value = """
            SELECT * FROM event_details order by event_date desc limit 10
            """,nativeQuery = true)
    List<Events> findAll();
}
