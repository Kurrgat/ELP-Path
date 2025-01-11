package com.example.emtechelppathbackend.events.eventsv2;

import com.example.emtechelppathbackend.eventsparticipators.EventsParticipatorsService;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsServicev2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/events")
public class EventsControllerv2 {

    private final ModelMapper modelMapper;
    private final EventsServicev2 eventsService;
    private final EventsParticipatorsServicev2 eventsParticipatorsService;


    @PostMapping("/{chapterId}/{userId}/create-chapter-event")
    public ResponseEntity<?> createChapterEvent
            (@ModelAttribute @Valid EventsDtov2 eventsDto,
             @RequestParam("eventImage") List<MultipartFile> eventImage, @PathVariable Long chapterId, @PathVariable Long userId) {
        try {
            EventsV2 event = modelMapper.map(eventsDto, EventsV2.class);
            var response = eventsService.addNewChapterEventv2(event, chapterId,userId, eventImage);

            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{hubId}/{userId}/create-hub-event")
    public ResponseEntity<?> createHubEvent
            (@ModelAttribute @Valid EventsDtov2 eventsDto,
             @RequestParam("eventImage") List<MultipartFile> eventImage, @PathVariable Long hubId,@PathVariable Long userId) {
        try {
            EventsV2 event = modelMapper.map(eventsDto, EventsV2.class);
            var response = eventsService.addNewHubEventv2(event, hubId,userId, eventImage);

            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-event")
    public ResponseEntity<?> createNonChapterEvent(
            @ModelAttribute @Valid EventsDtov2 eventsDto) {
        try {
            List<MultipartFile> images = eventsDto.getEventImage();
            System.out.println("event images are"+images);
            EventsV2 event = modelMapper.map(eventsDto, EventsV2.class);
            var response = eventsService.addNewEventv2(event, images);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/participate/{eventId}")
    public ResponseEntity<?> participateEvent(@PathVariable Long userId, @PathVariable Long eventId){
        try {
            eventsParticipatorsService.participateEvent(userId, eventId);
            return new ResponseEntity<>(new ResponseRecord("participation recorded successfully", null), HttpStatus.OK);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), "processed"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/cancel-participation/{eventId}")
    public ResponseEntity<?> cancelParticipation(@PathVariable Long userId, @PathVariable Long eventId){

           var response= eventsParticipatorsService.cancelParticipation(userId, eventId);
            return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/all")
    public ResponseEntity<?> displayAllEvents() {
        try {
            var events = eventsService.displayAllEventsv2();
            return ResponseEntity.status(events.getStatusCode()).body(events);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("{chapterId}/display-chapter-events")
    public ResponseEntity<?> displayEventsByChapterId(@PathVariable Long chapterId) {
            var response = eventsService.displayEventsByChapterId(chapterId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{hubId}/display-hub-events")
    public ResponseEntity<?> displayEventsByHubId(@PathVariable Long hubId) {
            var response = eventsService.displayEventsByHubId(hubId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{chapterId}/count-chapter-events")
    public ResponseEntity<?> getTotalEventsByChapterId(@PathVariable Long chapterId) {
        var response = eventsService.getTotalEventsByChapterId(chapterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/{chapterId}/count-hub-events")
    public ResponseEntity<?> getTotalEventsByHubId(@PathVariable Long hubId) {
        var response = eventsService.getTotalEventsByHubId(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-all-events")
    public ResponseEntity<?> getTotalEvents() {
        var response = eventsService.getTotalEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/display-scheduled-events")
    public ResponseEntity<?> displayScheduledEvents() {
        var response = eventsService.displayScheduledEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-scheduled-events")
    public ResponseEntity<?> getTotalScheduledEvents() {
        var response = eventsService.getTotalScheduledEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/display-past-events")
    public ResponseEntity<?> displayPastEvents() {
        var response = eventsService.displayPastEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-past-events")
    public ResponseEntity<?> getTotalPastEvents() {
        var response = eventsService.getTotalPastEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/display-active-events")
    public ResponseEntity<?> displayActiveEvents() {
        var response = eventsService.displayActiveEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("count-active-events")
    public ResponseEntity<?> getTotalActiveEvents() {
        var response = eventsService.getTotalActiveEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/display-events-by-date")
    public ResponseEntity<?> displayEventsByDate(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        var response = eventsService.displayEventsByDate(startDate, endDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{eventDate}/count-events-by-date")
    public ResponseEntity<?> getTotalEventsByDate(@PathVariable LocalDate eventDate) {
        var response = eventsService.getTotalEventsByDate(eventDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("update/{eventId}")
    public ResponseEntity<?> updateEventById(
            @ModelAttribute @Valid EventsDtov2 updatedEventDto, @PathVariable Long eventId, @RequestParam List<MultipartFile> eventImage) {
        try {
            var response = eventsService.updateEventByIdv2(eventId, updatedEventDto, eventImage);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{eventId}/delete")
    public ResponseEntity<?> deleteEventById(@PathVariable Long eventId) {
        try {
            var response = eventsService.deleteEventByIdv2(eventId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/chapter_events/{userId}")
    public ResponseEntity<?> getChapterEvents(@PathVariable Long userId) {
        var response = eventsService.getChapterEvents(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/hub_events/{userId}")
    public ResponseEntity<?> getHubEvents(@PathVariable Long userId) {
        var response = eventsService.getHubEvents(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-events-subscribed/{userId}")
    public ResponseEntity<?> getEventSubscriber(@PathVariable Long userId) {
        var response = eventsService.getEventSubscriber(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/event-subscribers/{eventId}")
    public ResponseEntity<?> getSubscribers(@PathVariable Long eventId) {
        var response = eventsService.getSubscribers(eventId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-events-for-approval")
    public ResponseEntity<?> getEventsForApproval() {
        var response = eventsService.getEventsForApproval();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/fetch-chapter-events-for-approval/{chapterId}")
    public ResponseEntity<?> getChapterEventsForApproval(@PathVariable Long chapterId) {
        var response = eventsService.getChapterEventsForApproval(chapterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/fetch-hub-events-for-approval/{hubId}")
    public ResponseEntity<?> getHubEventsForApproval(@PathVariable Long hubId) {
        var response = eventsService.getHubEventsForApproval(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/approve-chapter-events/{eventId}")
    public ResponseEntity<?> approveChapterEvents(@PathVariable Long eventId,@RequestParam boolean b) {
        var response = eventsService.approveChapterEvents(eventId, b);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/approve-hub-events/{eventId}")
    public ResponseEntity<?> approveHubEvents(@PathVariable Long eventId,@RequestParam boolean approve) {
        var response = eventsService.approveHubEvents(eventId, approve);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-event/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable  Long eventId) {
        var response = eventsService.getEvent(eventId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
