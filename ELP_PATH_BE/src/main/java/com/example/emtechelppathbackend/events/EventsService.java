//package com.example.emtechelppathbackend.events;
//
//import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
//import com.example.emtechelppathbackend.utils.CustomResponse;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public interface EventsService {
//    //CRUD
//
//    void addNewChapterEvent(Events event, Long chapterId, MultipartFile eventImage) throws NoResourceFoundException, IOException;
//
//    void addNewHubEvent(Events event, Long hubId, MultipartFile eventImage) throws NoResourceFoundException, IOException;
//
//    void addNewEvent(Events event, MultipartFile eventImage) throws  IOException;
//
//    CustomResponse<List<Events>> displayAllEvents() throws NoResourceFoundException;
//
//    CustomResponse<?> displayEventById(Long eventId) throws NoResourceFoundException;
//
//    void updateEventById(Long eventId, EventsDto updatedEventDTO, MultipartFile eventImage) throws NoResourceFoundException, IOException;
//
//    void deleteEventById(Long eventId) throws NoResourceFoundException;
//
//    //END CRUD
//
//    //QUERYING AND FILTERING
//
//
//
//    List<Events> displayEventsByHubId(Long chapterId) throws NoResourceFoundException;
//
//    CustomResponse<?> getTotalEventsByChapterId(Long chapterId);
//
//    CustomResponse<?> getTotalEventsByHubId(Long hubId);
//
//
//    CustomResponse<List<Events>> displayEventsWithOutChapter();
//
//    CustomResponse<?>  getTotalEventsWithOutChapters();
//
//    CustomResponse<List<Events>> displayScheduledEvents();
//
//    CustomResponse<?> getTotalScheduledEvents();
//
//    CustomResponse<List<Events>> displayPastEvents();
//
//    CustomResponse<?> getTotalPastEvents();
//
//    CustomResponse<List<Events>> displayActiveEvents();
//
//    CustomResponse<?> getTotalActiveEvents();
//
//    CustomResponse<List<Events>> displayEventsByDate(LocalDate eventDate);
//
//    CustomResponse<?> getTotalEventsByDate(LocalDate eventDate);
//
//    void updateEventStatus(Events events, String eventDateString);
//
//
//    CustomResponse<?> getTotalEvents();
//
//    CustomResponse<?> displayEventsByChapterId(Long chapterId);
//
//    void updateEventStatus(Events event);
//}
