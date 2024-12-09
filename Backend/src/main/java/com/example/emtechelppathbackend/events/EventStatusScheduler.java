package com.example.emtechelppathbackend.events;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    private final EventsService eventService;
    private final EventsRepo eventsRepository;

    @Scheduled(fixedRate = 60000)//60000 milliseconds so as to update every 1 minute
    public void updateActivityStatuses() {
        List<Events> events = eventsRepository.findAll();

        //filtering only the events which are not already updated to past
        List<Events> eventsToUpdate = events.stream()
                .filter(activity -> activity.getEventStatus() != EventStatus.PAST)
                .toList();

        for (Events event : eventsToUpdate) {
            eventService.updateEventStatus(event);
            eventsRepository.save(event);
        }
    }
}


