package com.example.emtechelppathbackend.events.eventsv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface EventsServicev2 {
    //CRUD
    
    CustomResponse<?> addNewChapterEventv2(EventsV2 event, Long chapterId, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException;

    CustomResponse<?> addNewHubEventv2(EventsV2 event, Long hubId, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException;

    CustomResponse<?> addNewEventv2(EventsV2 event, List<MultipartFile> eventImages) throws  IOException;

    CustomResponse<List<EventsV2>> displayAllEventsv2() throws NoResourceFoundException;

    CustomResponse<EventsV2> displayEventByIdv2(Long eventId) throws NoResourceFoundException;
    
    CustomResponse<?> updateEventByIdv2(Long eventId, EventsDtov2 updatedEventDTO, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException;

    CustomResponse<?> deleteEventByIdv2(Long eventId) throws NoResourceFoundException;

    //END CRUD

    //QUERYING AND FILTERING

    CustomResponse<?> getTotalEvents();

    CustomResponse<List<EventsV2>> displayEventsByChapterId(Long chapterId) throws NoResourceFoundException;

    CustomResponse<List<EventsV2>> displayEventsByHubId(Long chapterId) throws NoResourceFoundException;

    CustomResponse<?> getTotalEventsByChapterId(Long chapterId);

    CustomResponse<?> getTotalEventsByHubId(Long hubId);

    CustomResponse<List<EventsV2>> displayEventsWithOutChapter();

    CustomResponse<?> getTotalEventsWithOutChapters();

    CustomResponse<List<EventsV2>> displayScheduledEvents();

    CustomResponse<?> getTotalScheduledEvents();

    CustomResponse<List<EventsV2>> displayPastEvents();

    CustomResponse<?> getTotalPastEvents();

    CustomResponse<List<EventsV2>> displayActiveEvents();

    CustomResponse<?> getTotalActiveEvents();

    CustomResponse<List<EventsV2>> displayEventsByDate(LocalDate eventDate);

    CustomResponse<?> getTotalEventsByDate(LocalDate eventDate);

    CustomResponse<?> updateEventStatus(EventsV2 events);

    CustomResponse<?> getChapterEvents(Long userId);
    CustomResponse<?>getHubEvents(Long userId);
}
