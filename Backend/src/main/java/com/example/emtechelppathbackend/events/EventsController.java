package com.example.emtechelppathbackend.events;

import com.example.emtechelppathbackend.eventsparticipators.EventsParticipatorsService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.utils.CustomResponse;
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
@RequestMapping("/events")
public class EventsController {

    private final ModelMapper modelMapper;
    private final EventsService eventsService;
    private final EventsParticipatorsService eventsParticipatorsService;


    @PostMapping("/{chapterId}/create-chapter-event")
    public ResponseEntity<?> createChapterEvent
            (@ModelAttribute @Valid EventsDto eventsDto,
             @RequestParam("eventImage") MultipartFile eventImage, @PathVariable Long chapterId) {
        try {
            Events event = modelMapper.map(eventsDto, Events.class);
            eventsService.addNewChapterEvent(event, chapterId, eventImage);

            return ResponseEntity.ok(new ResponseRecord("Event created successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{hubId}/create-hub-event")
    public ResponseEntity<?> createHubEvent
            (@ModelAttribute @Valid EventsDto eventsDto,
             @RequestParam("eventImage") MultipartFile eventImage, @PathVariable Long hubId) {
        try {
            Events event = modelMapper.map(eventsDto, Events.class);
            eventsService.addNewHubEvent(event, hubId, eventImage);

            return ResponseEntity.ok(new ResponseRecord("Event created successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-event")
    public ResponseEntity<?> createNonChapterEvent(
            @ModelAttribute @Valid EventsDto eventsDto, @RequestParam MultipartFile eventImage) {
        try {
            Events event = modelMapper.map(eventsDto, Events.class);
            eventsService.addNewEvent(event, eventImage);
            return ResponseEntity.ok(new ResponseRecordOFMessages("Event created successfully", "Image Uploaded Successfully"));
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
        try {
            eventsParticipatorsService.cancelParticipation(userId, eventId);
            return new ResponseEntity<>(new ResponseRecord("participation updated successfully", null), HttpStatus.OK);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), "processed"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> displayAllEvents() {

        var events = eventsService.displayAllEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/{eventId}/display")
    public ResponseEntity<?> displayEventById(@PathVariable Long eventId) {

        var event = eventsService.displayEventById(eventId);
        EventsDTOView responseDTO = modelMapper.map(event, EventsDTOView.class);
        return ResponseEntity.status(event.getStatusCode()).body(event);

    }

    @GetMapping("{chapterId}/display-chapter-events")
    public ResponseEntity<?> displayEventsByChapterId(@PathVariable Long chapterId) {

        var events = eventsService.displayEventsByChapterId(chapterId);
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("{hubId}/display-hub-events")
    public ResponseEntity<?> displayEventsByHubId(@PathVariable Long hubId) {
        try {
            List<Events> events = eventsService.displayEventsByHubId(hubId);
            return ResponseEntity.ok(events);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{chapterId}/count-chapter-events")
    public ResponseEntity<?> getTotalEventsByChapterId(@PathVariable Long chapterId) {
        var events= eventsService.getTotalEventsByChapterId(chapterId);
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/{chapterId}/count-hub-events")
    public ResponseEntity<?> getTotalEventsByHubId(@PathVariable Long hubId) {
        var response= eventsService.getTotalEventsByHubId(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-all-events")
    public ResponseEntity<?> getTotalEvents() {
        var events= eventsService.getTotalEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }


    @GetMapping("/display-scheduled-events")
    public ResponseEntity<?> displayScheduledEvents() {
        var events=eventsService.displayScheduledEvents();

        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/count-scheduled-events")
    public ResponseEntity<?> getTotalScheduledEvents() {
        var events= eventsService.getTotalScheduledEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/display-past-events")
    public ResponseEntity<CustomResponse<List<Events>>> displayPastEvents() {
        var events= eventsService.displayPastEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/count-past-events")
    public ResponseEntity<?> getTotalPastEvents() {
        var events= eventsService.getTotalPastEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/display-active-events")
    public ResponseEntity<?> displayActiveEvents() {
        var events= eventsService.displayActiveEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("count-active-events")
    public ResponseEntity<?> getTotalActiveEvents() {
        var events= eventsService.getTotalActiveEvents();
        return ResponseEntity.status(events.getStatusCode()).body(events);

    }

    @GetMapping("/{eventDate}/display-events-by-date")
    public ResponseEntity<CustomResponse<List<Events>>> displayEventsByDate(@PathVariable LocalDate eventDate) {
        var events= eventsService.displayEventsByDate(eventDate);
        ResponseEntity.status(events.getStatusCode()).body(events);
        return ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @GetMapping("/{eventDate}/count-events-by-date")
    public ResponseEntity<?> getTotalEventsByDate(@PathVariable LocalDate eventDate) {
        var events= eventsService.getTotalEventsByDate(eventDate);
        return  ResponseEntity.status(events.getStatusCode()).body(events);
    }

    @PutMapping("update/{eventId}")
    public ResponseEntity<?> updateEventById(
            @ModelAttribute @Valid EventsDto updatedEventDto, @PathVariable Long eventId, @RequestParam MultipartFile eventImage) {
        try {
            eventsService.updateEventById(eventId, updatedEventDto, eventImage);
            return ResponseEntity.ok(new ResponseRecordOFMessages("event updated successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{eventId}/delete")
    public ResponseEntity<?> deleteEventById(@PathVariable Long eventId) {
        try {
            eventsService.deleteEventById(eventId);
            return new ResponseEntity<>(new ResponseRecord("event deleted successfully", null), HttpStatus.OK);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

