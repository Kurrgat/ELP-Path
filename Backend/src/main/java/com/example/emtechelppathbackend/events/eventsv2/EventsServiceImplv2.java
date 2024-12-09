package com.example.emtechelppathbackend.events.eventsv2;

import com.example.emtechelppathbackend.chapter.ChapterRepoV2;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.chapter.chapterv2.ChapterServicev2;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsServicev2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.hubs.hubsv2.HubsRepov2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsServiceImplv2 implements EventsServicev2 {


    private final EventsRepov2 eventsRepository;
    private final ChapterRepoV2 chapterRepo;
    private final HubsRepov2 hubsRepo;
    private final EventsParticipatorsServicev2 eventsParticipatorsService;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;
    private final JobOpportunityService jobOpportunityService;
    private final UsersRepository usersRepository;
    private final ChapterRepoV2 chapterRepoV2;
    private final HubMemberRepov2 hubMemberRepov2;
    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;

    @Autowired
    ServerPortService serverPortService;


    @Override
    public CustomResponse<?> addNewChapterEventv2(EventsV2 event, Long chapterId, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();



        try {
            Optional<ChapterV2> optionalChapter = chapterRepo.findById(chapterId);

            if(optionalChapter.isEmpty()) {
                response.setMessage("chapter with id "+chapterId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                event.setChapter(optionalChapter.get());

                if(eventImages != null && !eventImages.isEmpty()) {
                    for(MultipartFile file: eventImages){
                        String fileName = file.getOriginalFilename();
                        String extension = fileName != null ? fileName.substring(fileName.lastIndexOf('.')) : null;
                        String uniqueFileName = UUID.randomUUID().toString() + extension;

                        images.add(uniqueFileName);
                        file.transferTo(uploadPath.resolve(uniqueFileName));
                    }

                }
                event.setEventImages(images);
                eventsRepository.save(event);
                response.setMessage("Event added successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(event);
                CompletableFuture.runAsync(() -> eventsParticipatorsService.notifyChapterMembersAboutANewChapterEvent(event));
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> addNewHubEventv2(EventsV2 event, Long hubId, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();

        try {
            Optional<Hubv2> optionalHub = hubsRepo.findById(hubId);

            if(optionalHub.isEmpty()) {
                response.setMessage("Hub with id "+hubId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                event.setHub(optionalHub.get());

                if(eventImages != null && !eventImages.isEmpty()) {
                    for(MultipartFile file: eventImages) {
                        String fileName = file.getOriginalFilename();
                        assert fileName != null;
                        String extension = fileName.substring(fileName.lastIndexOf('.'));
                        String uniqueFileName = UUID.randomUUID().toString() + extension;

                        images.add(uniqueFileName);

                        file.transferTo(uploadPath.resolve(uniqueFileName));
                    }

                    event.setEventImages(images);
                    eventsRepository.save(event);

                    response.setMessage("Hub Event added successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setPayload(event);

                } else {
                    event.setEventImages(images);
                    eventsRepository.save(event);

                    response.setMessage("Hub event saved successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setPayload(event);
                }
                response.setStatusCode(HttpStatus.OK.value());
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Hub event saved successfully");
                response.setPayload(event);
                CompletableFuture.runAsync(()-> eventsParticipatorsService.notifyHubMembersAboutANewHubEvent(event));
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<?> addNewEventv2(EventsV2 event, List<MultipartFile> eventImages) throws IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();
        event.setChapter(null);

        try {
            if(eventImages != null && !eventImages.isEmpty()) {
                for(MultipartFile file: eventImages) {
                    String fileName = file.getOriginalFilename();

                    if(fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                        String extension = fileName != null ? fileName.substring(fileName.lastIndexOf('.')) : null;
                        String uniqueFileName = UUID.randomUUID().toString() + extension;

                        images.add(uniqueFileName);

                        file.transferTo(uploadPath.resolve(uniqueFileName));

                        event.setEventImages(images);
                        eventsRepository.save(event);

                        response.setMessage("Event added successfully");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setPayload(event);
                    } else {
                        response.setMessage("Upload files of type .jpeg, .jpg or .png");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    }
                }

            }
            event.setEventImages(images);
            eventsRepository.save(event);

            response.setMessage("Event added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(event);
            CompletableFuture.runAsync(() -> eventsParticipatorsService.notifyUsersAboutANewEvent(event));
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayAllEventsv2() throws NoResourceFoundException {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        try {
            List<EventsV2> events = eventsRepository.findAll();

            if(events.isEmpty()) {
                response.setMessage("No events found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(events);
            } else {
                for(EventsV2 event: events) {
                    if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                        Set<String> imageUrls = event.getEventImages().stream().map(imageName -> jobOpportunityService.getImagesPath() + imageName).collect(Collectors.toSet());
                        event.setEventImages(imageUrls);
                    }
                }
                response.setMessage("Found "+events.size()+" events");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(events);
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<EventsV2> displayEventByIdv2(Long eventId) throws NoResourceFoundException {
        CustomResponse<EventsV2> response = new CustomResponse<>();

        try {
            Optional<EventsV2> optional = eventsRepository.findById(eventId);

            if(optional.isEmpty()) {
                response.setMessage("Event with id "+eventId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                EventsV2 event = optional.get();

                if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                    Set<String> imageURLs = event.getEventImages().stream().map(imageName -> jobOpportunityService.getImagesPath() + imageName).collect(Collectors.toSet());
                    event.setEventImages(imageURLs);
                }

                response.setMessage("Found event");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(event);
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateEventByIdv2(Long eventId, EventsDtov2 updatedEventDTO, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();

        try {
            Optional<EventsV2> optionalEvent = eventsRepository.findById(eventId);
            if(optionalEvent.isEmpty()) {
                response.setMessage("Event with id "+eventId+" not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                EventsV2 existingEvent = optionalEvent.get();
                modelMapper.map(updatedEventDTO, existingEvent);

                if(eventImages != null && !eventImages.isEmpty()) {
                    for(MultipartFile file: eventImages) {
                        String fileName = file.getOriginalFilename();
                        assert fileName != null;
                        String extension = fileName.substring(fileName.lastIndexOf('.'));
                        String uniqueFileName = UUID.randomUUID().toString() + extension;

                        images.add(uniqueFileName);

                        file.transferTo(uploadPath.resolve(uniqueFileName));
                    }
                }
                existingEvent.setEventImages(images);
                eventsRepository.save(existingEvent);

                response.setMessage("Event with id"+eventId+" updated!");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(existingEvent);
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> deleteEventByIdv2(Long eventId) throws NoResourceFoundException {
        CustomResponse<?> response = new CustomResponse<>();

        try {
            if(!eventsRepository.existsById(eventId)) {
                response.setMessage("Event with id "+eventId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                eventsRepository.deleteById(eventId);

                response.setMessage("Event with id "+eventId+" deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    //END OF CRUD

    @Override
    public CustomResponse<?> getTotalEvents() {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT(a) FROM Eventsv2 a";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            var results = query.getSingleResult();

            response.setMessage("Found "+results+" Events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayEventsByChapterId(Long chapterId) throws NoResourceFoundException {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        try {
            List<EventsV2> possibleEvents = eventsRepository.findEventsByChapterId(chapterId);
            if (possibleEvents.isEmpty()) {
                response.setMessage("This Chapter has no events associated to it");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(possibleEvents);
            } else {
                Optional<ChapterV2> optionalC = chapterRepo.findById(chapterId);
                ChapterV2 chapter = optionalC.get();
                for(EventsV2 event:possibleEvents) {
                    if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                        Set<String> eventImages = event.getEventImages().stream().map(eventImage -> jobOpportunityService.getImagesPath()+eventImage).collect(Collectors.toSet());
                        event.setEventImages(eventImages);
                    }
                }
                response.setMessage("Found "+possibleEvents.size()+" Events for "+chapter.getChapterName()+" chapter");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(possibleEvents);
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayEventsByHubId(Long hubId) throws NoResourceFoundException {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        try {      
            List<EventsV2> possibleEvents = eventsRepository.findEventsByHubId(hubId);
            if (possibleEvents.isEmpty()) {
                response.setMessage("This hub has no events associated to it");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(possibleEvents);
            } else {
                Optional<Hubv2> optionalH = hubsRepo.findById(hubId);
                Hubv2 hub = optionalH.get();
                for(EventsV2 event:possibleEvents) {
                    if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                        Set<String> eventImages = event.getEventImages().stream().map(eventImage -> jobOpportunityService.getImagesPath() + eventImage).collect(Collectors.toSet());
                        event.setEventImages(eventImages);
                    }
                }
                response.setMessage("Found "+possibleEvents.size()+" Events for "+hub.getHubName()+" hub");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(possibleEvents);;
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTotalEventsByHubId(Long hubId) {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT(a) FROM Eventsv2 a WHERE a.hub.id = :hubId";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            query.setParameter("hubId", hubId);
            var result =  query.getSingleResult();

            response.setMessage("Found "+result+" Events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTotalEventsByChapterId(Long chapterId) {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT(a) FROM Eventsv2 a WHERE a.chapter.id = :chapterId";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            query.setParameter("chapterId", chapterId);
            var result =  query.getSingleResult();

            response.setMessage("Found "+result+" Events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayEventsWithOutChapter() {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();
        
        try {
            String queryString = "SELECT a FROM Eventsv2 a WHERE a.chapter IS NULL";
            TypedQuery<EventsV2> query = entityManager.createQuery(queryString, EventsV2.class);
            var results = query.getResultList();

            for(EventsV2 event:results) {
                if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                    Set<String> eventImages = event.getEventImages().stream().map(eventImage -> jobOpportunityService.getImagesPath()+eventImage).collect(Collectors.toSet());
                    event.setEventImages(eventImages);
                }
            }

            response.setMessage("Found "+results.size()+" without chapters");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTotalEventsWithOutChapters() {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT(a) FROM Eventsv2 a WHERE a.chapter IS NULL";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            var result =  query.getSingleResult();

            response.setMessage("Found "+result+" Events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayScheduledEvents() {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();
        
        try {
            String queryString = "SELECT a FROM Eventsv2 a WHERE a.eventStatus =:status";
            TypedQuery<EventsV2> query = entityManager.createQuery(queryString, EventsV2.class);
            query.setParameter("status", EventStatusv2.SCHEDULED);
            var results = query.getResultList();

            for(EventsV2 event: results) {
                if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                    Set<String> eventImages = event.getEventImages().stream().map(imageName->getImagesPath()+imageName).collect(Collectors.toSet());
                    event.setEventImages(eventImages);
                }
            }

            response.setMessage("Found "+results.size()+" scheduled events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTotalScheduledEvents() {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT (a) FROM Eventsv2 a WHERE a.eventStatus = :status";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            query.setParameter("status", EventStatusv2.SCHEDULED);
            var result = query.getSingleResult();

            response.setMessage("Found "+result+" scheduled events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayPastEvents() {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        try {
            String queryString = "SELECT a FROM Eventsv2 a WHERE a.eventStatus =:status";
            TypedQuery<EventsV2> query = entityManager.createQuery(queryString, EventsV2.class);
            query.setParameter("status", EventStatusv2.PAST);
            var results = query.getResultList();

            for(EventsV2 event: results) {
                if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                    Set<String> eventImages = event.getEventImages().stream().map(imageName->jobOpportunityService.getImagesPath()+imageName).collect(Collectors.toSet());
                    event.setEventImages(eventImages);
                }
            }

            response.setMessage("Found "+results.size()+" past events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTotalPastEvents() {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT (a) FROM Eventsv2 a WHERE a.eventStatus = :status";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            query.setParameter("status", EventStatusv2.PAST);
            var result = query.getSingleResult();

            response.setMessage("Found "+result+" past events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayActiveEvents() {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();
        
        try {
            String queryString = "SELECT a FROM Eventsv2 a WHERE a.eventStatus =:status";
            TypedQuery<EventsV2> query = entityManager.createQuery(queryString, EventsV2.class);
            query.setParameter("status", EventStatusv2.ONGOING);
            var results = query.getResultList();

            for(EventsV2 event: results) {
                if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                    Set<String> eventImages = event.getEventImages().stream().map(imageName->jobOpportunityService.getImagesPath()+imageName).collect(Collectors.toSet());
                    event.setEventImages(eventImages);
                }
            }

            response.setMessage("Found "+results.size()+" active events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return  response;
    }

    @Override
    public CustomResponse<?> getTotalActiveEvents() {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            String queryString = "SELECT COUNT (a) FROM Eventsv2 a WHERE a.eventStatus = :status";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            query.setParameter("status", EventStatusv2.ONGOING);
            var result = query.getSingleResult();

            response.setMessage("Found "+result+" active events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayEventsByDate(LocalDate eventDate) {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        //convert LocalDate to localDateTime by setting time to midnight
        LocalDateTime startDateTime = eventDate.atStartOfDay();

        //Calculate the end of the day
        LocalDateTime endDateTime = eventDate.atTime(LocalTime.MAX);

        try {
            String queryString = "SELECT a FROM Eventsv2 a WHERE a.eventDate >= :startDateTime AND a.eventDate <= :endDateTime";
            TypedQuery<EventsV2> query = entityManager.createQuery(queryString, EventsV2.class);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);

            var results = query.getResultList();

            for(EventsV2 event:results) {
                if(event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                    Set<String> eventImages = event.getEventImages().stream().map(imageName -> jobOpportunityService.getImagesPath()+imageName).collect(Collectors.toSet());
                    event.setEventImages(eventImages);
                }
            }

            response.setMessage("Found "+results.size()+" events");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        } finally {
            entityManager.close();
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTotalEventsByDate(LocalDate eventDate) {
        CustomResponse<Long> response = new CustomResponse<>();

        // Convert LocalDate to LocalDateTime by setting time to midnight
        LocalDateTime startDateTime = eventDate.atStartOfDay();

        // Calculate the end of the day
        LocalDateTime endDateTime = eventDate.atTime(LocalTime.MAX);
        try {
            String queryString = "SELECT COUNT(a) FROM Eventsv2 a WHERE a.eventDate >= :startDateTime AND a.eventDate <= :endDateTime";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);

            var results = query.getSingleResult();

            response.setMessage("Found "+results+" events happening on "+eventDate);
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(results);
        } catch(Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        } finally {
            entityManager.close();
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateEventStatus(EventsV2 event) {
        CustomResponse<String> response = new CustomResponse<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activityDateTime = event.getEventDate();

        try {
            if (activityDateTime.isBefore(now.minusHours(8))) {
                event.setEventStatus(EventStatusv2.PAST);
            } else if (activityDateTime.isAfter(now.plusHours(1))) {
                event.setEventStatus(EventStatusv2.SCHEDULED);
            } else {
                event.setEventStatus(EventStatusv2.ONGOING);
            }

            response.setMessage("Event status updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload("Status: "+event.getEventStatus());
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
            return response;
    }



    @Override
    public CustomResponse<?> getChapterEvents(Long userId) {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);


            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                Scholar scholar = user.getScholar();
                if (scholar != null) {
                    // Try to find chapter by institutional or regional IDs
                    ChapterV2 chapter = null;
                    if (scholar.getInstitution() != null && scholar.getInstitution().getId() != null) {
                        chapter = chapterRepoV2.findById(scholar.getInstitution().getId()).orElse(null);

                    }
                    if (chapter == null && scholar.getHomeCounty() != null) {
                        chapter = chapterRepoV2.findById(scholar.getHomeCounty().getId()).orElse(null);
                    }

                    if (chapter != null) {
                        List<EventsV2> events = eventsRepository.findEventsByChapterId(chapter.getId());
                        System.out.println("events are........." + events);
                        if (events == null || events.isEmpty()) {
                            response.setMessage("No events found for user: "  + userId);
                            response.setSuccess(false);
                            response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        } else {

                            response.setMessage("Events found for user: " + userId);
                            response.setSuccess(true);
                            response.setStatusCode(HttpStatus.OK.value());
                            response.setPayload(events);
                        }
                    } else {
                        response.setMessage("Chapter not found for user " + userId);
                        response.setSuccess(false);
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    }
                } else {
                    response.setMessage("Scholar not found for user " + userId);
                    response.setSuccess(false);
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                }
            } else {
                response.setMessage("User not found with ID: " + userId);
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getHubEvents(Long userId) {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);

            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                Scholar scholar = user.getScholar();
                if (scholar != null) {
                    // Check if the user exists in the hub_member
                    Optional<HubMemberv2> hubMemberOptional = hubMemberRepov2.findById(user.getId());
                    if (hubMemberOptional.isPresent()) {
                        HubMemberv2 hubMember = hubMemberOptional.get();
                        if (hubMember.getHub() != null) {
                            // If the user is associated with a hub and the hub is not null
                            Hubv2 hub = hubMember.getHub();
                            List<EventsV2> hubEvents = eventsRepository.findEventsByHubId(hub.getId());
                            if (!hubEvents.isEmpty()) {
                                response.setMessage("Hub events found for user " + userId);
                                response.setSuccess(true);
                                response.setStatusCode(HttpStatus.OK.value());
                                response.setPayload(hubEvents);
                            } else {
                                response.setMessage("No hub events found for user " + userId);
                                response.setSuccess(false);
                                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                            }
                        } else {
                            response.setMessage("User is associated with a hub, but the hub is null");
                            response.setSuccess(false);
                            response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        }
                    } else {
                        response.setMessage("User is not associated with any hub");
                        response.setSuccess(false);
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    }
                } else {
                    response.setMessage("Scholar not found for user " + userId);
                    response.setSuccess(false);
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                }
            } else {
                response.setMessage("User not found with ID: " + userId);
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            try {
                int port = serverPortService.getPort();

                if(port > 1023) {
                    imagesPath = hostNameCapture.getHost()+":"+port+"/images/";
                } else {
                    log.debug("Port is reservered for system use");
                }
                // imagesPath = hostNameCapture.getHost()+":5555/images/";
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagesPath;
    }

}
