package com.example.emtechelppathbackend.events.eventsv2;

import com.example.emtechelppathbackend.chapter.ChapterRepoV2;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.chapter.chapterv2.ChapterServicev2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberRepositoryv2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsRepositoryv2;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsServicev2;
import com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2.EventsParticipatorsv2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.hubs.hubsv2.HubsRepov2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.notification.Notification;
import com.example.emtechelppathbackend.notification.NotificationRepository;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private final EventsParticipatorsRepositoryv2 eventsParticipatorsRepo;
    private  final ChapterMemberRepositoryv2 chapterMemberRepository;

    private final NotificationRepository notificationRepository;
    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;

    @Autowired
    ServerPortService serverPortService;


    @Override
    public CustomResponse<?> addNewChapterEventv2(EventsV2 event, Long chapterId, Long userId, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();
        try {
            Optional<ChapterV2> optionalChapter = chapterRepo.findById(chapterId);
            Optional<Users> userOptional = usersRepository.findById(userId);

            if (optionalChapter.isEmpty()) {
                response.setMessage("Chapter with id " + chapterId + " does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            } else if (userOptional.isEmpty()) {
                response.setMessage("User with id " + userId + " does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            Users user = userOptional.get();
            event.setChapter(optionalChapter.get());
            event.setUsers(user);

            event.setApprovalType(ApprovalType.Chapter);
            if (eventImages != null && !eventImages.isEmpty()) {
                for (MultipartFile file : eventImages) {
                    String fileName = file.getOriginalFilename();
                    assert fileName != null;
                    String extension = fileName.substring(fileName.lastIndexOf('.'));
                    String uniqueFileName = UUID.randomUUID().toString()+ extension;

                    images.add(uniqueFileName);
                    file.transferTo(uploadPath.resolve(uniqueFileName));
                }
                event.setEventImages(images);
            } else {
                event.setEventImages(images);
            }

            // Check if the event name already exists for the provided chapter
            Optional<EventsV2> existingEvent = eventsRepository.findByEventName(event.getEventName());
            if (existingEvent.isPresent()) {
                response.setMessage("An event with the same name already exists for this chapter");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Rest of your existing logic for adding a new event...

            // Save the event
            eventsRepository.save(event);

            // Send notification if the event is approved
            if (event.getApprovalStatus() == EventApprovalStatus.APPROVED) {
                CompletableFuture.runAsync(() -> eventsParticipatorsService.notifyChapterMembersAboutANewChapterEvent(event))
                        .exceptionally(ex -> {
                            System.err.println("Error in sending approval notification: " + ex.getMessage());
                            return null; // Handling exception inside CompletableFuture
                        });
            }

            // Set response details
            response.setMessage("Chapter event " + (event.getApprovalStatus() == EventApprovalStatus.APPROVED ? "approved" : "sent for approval"));
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(event);

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> addNewHubEventv2(EventsV2 event, Long hubId, Long userId, List<MultipartFile> eventImages) throws NoResourceFoundException, IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();

        try {
            Optional<Hubv2> optionalHub = hubsRepo.findById(hubId);
            Optional<Users> optionalUser = usersRepository.findById(userId);

            if (optionalHub.isEmpty()) {
                response.setMessage("Hub with id " + hubId + " does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            if (optionalUser.isEmpty()) {
                response.setMessage("User with id " + userId + " does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }
            Users user = optionalUser.get();

            // Check if the event name already exists for the provided hub
            Optional<EventsV2> existingEvent = eventsRepository.findByEventName(event.getEventName());
            if (existingEvent.isPresent()) {
                response.setMessage("An event with the same name already exists for this hub");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            event.setHub(optionalHub.get());
            event.setApprovalType(ApprovalType.Hub);
            event.setUsers(user);

            if (eventImages != null && !eventImages.isEmpty()) {
                for (MultipartFile file : eventImages) {
                    String fileName = file.getOriginalFilename();
                    assert fileName != null;
                    String extension = fileName.substring(fileName.lastIndexOf('.'));
                    String uniqueFileName = UUID.randomUUID().toString() + extension;

                    images.add(uniqueFileName);
                    file.transferTo(uploadPath.resolve(uniqueFileName));
                }
                event.setEventImages(images);
            } else {
                event.setEventImages(images);
            }

            Optional<HubMemberv2> optionalHubMember = hubMemberRepov2.findByHubIdAndUserId(hubId, userId);
            if (optionalHubMember.isEmpty()) {
                response.setMessage("User is not a member of this hub or not an active member");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                // Set additional properties
                event.setEventName(event.getEventName());
                event.setOrganizer(event.getOrganizer());
                event.setEventLink(event.getEventLink());
                event.setEventDescription(event.getEventDescription());
                event.setEventDate(event.getEventDate());
                event.setLocation(event.getLocation());

                // Check user roles and set approval status accordingly before handling images
                if (!user.getScholar().getId().equals(optionalHub.get().getHubAdmin().getId()) || !user.getRole().getRoleName().equals("HUB_ADMIN")) {
                    // Set approval status to pending since the user is not the hub admin of the provided hub
                    event.setApprovalStatus(EventApprovalStatus.PENDING);
                } else {
                    // Set approval status to approved for hub admins of the provided hub
                    event.setApprovalStatus(EventApprovalStatus.APPROVED);
                }

                eventsRepository.save(event);

                // Notify hub members about the new event if it is approved automatically
                if (event.getApprovalStatus() == EventApprovalStatus.APPROVED) {
                    CompletableFuture.runAsync(() -> eventsParticipatorsService.notifyHubMembersAboutANewHubEvent(event))
                            .exceptionally(ex -> {
                                System.err.println("Error in sending approval notification: " + ex.getMessage());
                                return null; // Handling exception inside CompletableFuture
                            });
                }

                response.setMessage("Hub Event " + (event.getApprovalStatus() == EventApprovalStatus.APPROVED ? "approved automatically" : "sent for approval"));
                response.setStatusCode(HttpStatus.OK.value());
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
    public CustomResponse<?> addNewEventv2(EventsV2 event, List<MultipartFile> eventImages) throws IOException {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        Set<String> images = new HashSet<>();
        event.setChapter(null);

        try {
            // Check if the event name already exists
            Optional<EventsV2> existingEvent = eventsRepository.findByEventName(event.getEventName());
            if (existingEvent.isPresent()) {
                response.setMessage("An event with the same name already exists");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            if(eventImages != null && !eventImages.isEmpty()) {
                for(MultipartFile file: eventImages) {
                    String fileName = file.getOriginalFilename();

                    if(fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                        String extension = fileName != null ? fileName.substring(fileName.lastIndexOf('.')) : null;
                        String uniqueFileName = UUID.randomUUID().toString() + extension;

                        images.add(uniqueFileName);

                        file.transferTo(uploadPath.resolve(uniqueFileName));
                    } else {
                        response.setMessage("Upload files of type .jpeg, .jpg or .png");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                        return response;
                    }
                }
            }

            event.setEventImages(images);
            event.setApprovalStatus(EventApprovalStatus.APPROVED);
            eventsRepository.save(event);

            response.setMessage("Event added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(event);

            // Send notification to users about the new event
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
            List<EventsV2> events = eventsRepository.findAllEvents();

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

                        System.out.println("image urls:"+imageUrls);
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
    public CustomResponse<?> displayEventsByChapterId(Long chapterId) throws NoResourceFoundException {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        try {
            List<EventsV2> possibleEvents = eventsRepository.findEventsByChapterId(chapterId);

            if (possibleEvents.isEmpty()) {
                response.setMessage("This Chapter has no events associated to it");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(possibleEvents);
            } else{
                response.setMessage("Found "+possibleEvents.size()+" Events for chapter");
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
            String queryString = "SELECT COUNT(*) FROM EventsV2 a WHERE a.chapter.id = :chapterId";
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
            String queryString = "SELECT a FROM EventsV2 a WHERE a.eventStatus =:status";
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
            String queryString = "SELECT COUNT (a) FROM EventsV2 a WHERE a.eventStatus = :status";
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
            List<EventsV2> pastEvents = eventsRepository.findByEventStatus(EventStatusv2.PAST);

            if (pastEvents.isEmpty()) {
                response.setMessage("No past events found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                response.setMessage("Past events retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(pastEvents);
            }
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


            List<EventsV2> activeEvents = eventsRepository.findByEventStatus(EventStatusv2.PAST);

            long count = activeEvents.size();

            // Set the response object
            response.setPayload(count);
            response.setMessage("Total past  events  counted successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);

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
            // Get current date
            LocalDateTime currentDate = LocalDateTime.now();

            // Execute the SQL query with the current date parameter
            List<EventsV2> activeEvents = eventsRepository.findByEventDate(currentDate);

            if (activeEvents.isEmpty()) {
                response.setMessage("No active events found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                response.setMessage("Active events retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(activeEvents);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<Long> getTotalActiveEvents() {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            // Step 1: Retrieve the active events
            LocalDateTime currentDate = LocalDateTime.now();
            List<EventsV2> activeEvents = eventsRepository.findByEventDate(currentDate);

            long count = activeEvents.size();

            // Set the response object
            response.setPayload(count);
            response.setMessage("Total active events fetched successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("Error retrieving data: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<EventsV2>> displayEventsByDate(LocalDate startDate, LocalDate endDate) {
        CustomResponse<List<EventsV2>> response = new CustomResponse<>();

        //convert LocalDate to localDateTime by setting time to midnight
        LocalDateTime startDateTime = startDate.atStartOfDay();

        //Calculate the end of the day
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        try {
            String queryString = "SELECT e FROM EventsV2 e WHERE e.eventDate >= :startDateTime AND e.eventDate <= :endDateTime";
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

        try {
            // Assuming event.getEventDate() returns a LocalDateTime, no need to parse.
            LocalDateTime activityDateTime = event.getEventDate();

            if (activityDateTime.isBefore(now.minusHours(8))) {
                event.setEventStatus(EventStatusv2.PAST);
            } else if (activityDateTime.isAfter(now.plusHours(1))) {
                event.setEventStatus(EventStatusv2.SCHEDULED);
            } else {
                event.setEventStatus(EventStatusv2.ONGOING);
            }

            response.setMessage("Event status updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload("Status: " + event.getEventStatus());
        } catch (Exception e) {
            // This catch block is now more general, as DateTimeParseException should no longer occur
            response.setMessage("Internal server error: " + e.getMessage());
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

    @Override
    public CustomResponse<?> getEventSubscriber(Long userId) {
        CustomResponse<List<EventsParticipatorsv2>> response = new CustomResponse<>();
        try {
            // Query the events participators table to find the entry for the given userId and eventId
            List<EventsParticipatorsv2> eventSubscriber = eventsParticipatorsRepo.findByUserId(  userId);

            if (eventSubscriber != null) {
                response.setPayload(eventSubscriber);
                response.setMessage("Event subscriber found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No subscriber found for the given event and user");
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
    public CustomResponse<List<EventsParticipatorsv2>> getSubscribers(Long eventId) {
        CustomResponse<List<EventsParticipatorsv2>> response = new CustomResponse<>();
        try {
            // Query the events participators table to find the entry for the given userId and eventId
            List<EventsParticipatorsv2> eventSubscriberList = eventsParticipatorsRepo.findByEventId(eventId);

            if (eventSubscriberList != null && !eventSubscriberList.isEmpty()) {
                response.setPayload(eventSubscriberList);
                response.setMessage("Event subscribers found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No subscribers found for the given event");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(Collections.emptyList()); // Return an empty list instead of null
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(Collections.emptyList()); // It's a good practice to not return null
        }
        return response;
    }

    @Override
    public CustomResponse<?> getEventsForApproval() {
        CustomResponse<List<EventsRepov2.ApprovalStatusInterface>> response = new CustomResponse<>();
        try {
            // Query the events repository to find events with a PENDING approval status
            List<EventsRepov2.ApprovalStatusInterface> eventList = eventsRepository.findByEventApprovalStatus(EventApprovalStatus.PENDING);

            if (eventList != null && !eventList.isEmpty()) {

                response.setPayload(eventList);
                response.setMessage("Pending events for approval found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No pending events for approval found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
        return response;
    }


    @Override
    public CustomResponse<?> approveChapterEvents(Long eventId, boolean b) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            // Attempt to find the event record by event ID
            Optional<EventsV2> optionalEvent = eventsRepository.findById(eventId);
            if (optionalEvent.isEmpty()) {
                response.setMessage("Event not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            EventsV2 event = optionalEvent.get();

            // If approving the request
            if (b) {
                // Check if the event is already approved
                if (event.getApprovalStatus() == EventApprovalStatus.APPROVED) {
                    response.setMessage("Event is already approved");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }

                // Update the event's status to APPROVED
                event.setApprovalStatus(EventApprovalStatus.APPROVED);
                response.setMessage("Chapter event approved");
            } else {
                // Update the event's status to DECLINED
                event.setApprovalStatus(EventApprovalStatus.DECLINED);
                response.setMessage("Approval declined");
            }

            // Save the updated event
            eventsRepository.save(event);

            // Fetch necessary data for notification
            ChapterV2 chapter = event.getChapter();
            Users user = event.getUsers();

            // Send notification based on approval status
            if (event.getApprovalStatus() == EventApprovalStatus.APPROVED) {
                CompletableFuture.runAsync(() -> eventsParticipatorsService.notifyChapterMembersAboutANewChapterEvent(event))
                        .exceptionally(ex -> {
                            System.err.println("Error in sending approval notification: " + ex.getMessage());
                            return null; // Handling exception inside CompletableFuture
                        });
            } else if (event.getApprovalStatus() == EventApprovalStatus.DECLINED) {
                CompletableFuture.runAsync(() -> saveChapterNotification(event, user, chapter, b))
                        .exceptionally(ex -> {
                            System.err.println("Error in sending decline notification: " + ex.getMessage());
                            return null; // Handling exception inside CompletableFuture
                        });
            }

            // Set response details
            response.setMessage(b ? "Chapter event approved" : "Approval declined");
            response.setPayload(b ? "Approval Status" : "Event approval declined");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    private void saveChapterNotification(EventsV2 event, Users user, ChapterV2 chapter, boolean b) {

        try {
            String message = b ? "Event approved for chapter: " + chapter.getChapterName() : "Event approval declined: " + chapter.getChapterName();
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUsers(user);
            notification.setEvents(event);
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to save notification: " + e.getMessage());
        }
    }

    @Override
    public CustomResponse<?> approveHubEvents(Long eventId, boolean approve) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            // Attempt to find the event by its ID
            Optional<EventsV2> optionalEvent = eventsRepository.findById(eventId);
            if (optionalEvent.isEmpty()) {
                response.setMessage("Event not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            EventsV2 event = optionalEvent.get();


            // Check if the event status needs to be updated
            if ((approve && event.getApprovalStatus() == EventApprovalStatus.APPROVED) ||
                    (!approve && event.getApprovalStatus() == EventApprovalStatus.DECLINED)) {
                response.setMessage("Hub event is already " + (approve ? "approved" : "declined"));
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Update the event's approval status based on the input
            event.setApprovalStatus(approve ? EventApprovalStatus.APPROVED : EventApprovalStatus.DECLINED);

            eventsRepository.save(event); // Save the updated event
            Hubv2 hub = event.getHub(); // Fetch the hub associated with the member
            Users user = event.getUsers();

            // Notification logic moved outside of the conditional block for clarity
            CompletableFuture.runAsync(() -> {
                if (approve) {
                    eventsParticipatorsService.notifyHubMembersAboutANewHubEvent(event);
                } else {
                    saveNotification(event,user,hub, approve);
                }
            }).exceptionally(ex -> {
                System.err.println("Error in sending notifications: " + ex.getMessage());
                return null; // Handling exception inside CompletableFuture
            });

            // Set response details
            response.setMessage(approve ? "Hub event approved" : "Approval declined");
            response.setPayload(approve ? "Approval Status" : "Event approval declined");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("Error processing request: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    private void saveNotification(EventsV2 event, Users user, Hubv2 hub, boolean approve) {

        try {
            String message = approve ? "Membership approved for Hub: " + hub.getHubName() : "Event approval declined: " + hub.getHubName();
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUsers(user);
            notification.setHubs(hub);
            notification.setEvents(event);
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to save notification: " + e.getMessage());
        }
    }


    @Override
    public CustomResponse<EventsV2> getEvent(Long eventId) {
        CustomResponse<EventsV2> response = new CustomResponse<>();
        try {
            // Query the event repository to find the event by eventId
            Optional<EventsV2> optionalEvent = eventsRepository.findById(eventId);

            if (optionalEvent.isPresent()) {
                EventsV2 event = optionalEvent.get();
                response.setPayload(event);
                response.setMessage("Event found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No  event found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getChapterEventsForApproval(Long chapterId) {
        CustomResponse<List<EventsRepov2.ChapterEventsApprovalView>> response = new CustomResponse<>();
        try {
            // Query the events repository to find events with a PENDING approval status
            List<EventsRepov2.ChapterEventsApprovalView> eventList = eventsRepository.findByEventApprovalStatusAndChapterId(chapterId);

            if (eventList != null && !eventList.isEmpty()) {

                response.setPayload(eventList);
                response.setMessage("Pending chapter events for approval found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No pending chapter events for approval found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getHubEventsForApproval(Long hubId) {
        CustomResponse<List<EventsRepov2.ChapterEventsApprovalView>> response = new CustomResponse<>();
        try {
            System.out.println("my event list");
            // Query the events repository to find events with a PENDING approval status
            List<EventsRepov2.ChapterEventsApprovalView> eventList = eventsRepository.findByEventApprovalStatusAndHubId(hubId);

            if (eventList != null && !eventList.isEmpty()) {

                response.setPayload(eventList);
                response.setMessage("Pending chapter events for approval found");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No pending chapter events for approval found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Server error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
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
                    log.debug("Port is reserved for system use");
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
