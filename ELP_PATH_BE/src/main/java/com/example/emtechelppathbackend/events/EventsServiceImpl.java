//package com.example.emtechelppathbackend.events;
//
//import com.example.emtechelppathbackend.chapter.Chapter;
//import com.example.emtechelppathbackend.chapter.ChapterRepo;
//import com.example.emtechelppathbackend.eventsparticipators.EventsParticipatorsService;
//import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
//import com.example.emtechelppathbackend.image.Image;
//import com.example.emtechelppathbackend.image.ImageService;
//import com.example.emtechelppathbackend.utils.CustomResponse;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//@RequiredArgsConstructor
//public class EventsServiceImpl implements EventsService {
//
//
//    private final EventsRepo eventsRepository;
//    private final ChapterRepo chapterRepo;
//    private final EventsParticipatorsService eventsParticipatorsService;
//    private final ModelMapper modelMapper;
//    private final EntityManager entityManager;
//    private final ImageService imageService;
//
//
//    @Override
//    public void addNewChapterEvent(Events event, Long chapterId, MultipartFile eventImage) throws NoResourceFoundException, IOException {
//
//        Chapter chapter = chapterRepo.findById(chapterId)
//                .orElseThrow(() -> new NoResourceFoundException("Chapter of ID " + chapterId + " does not exist"));
//
//        event.setChapter(chapter);
//
//        if (eventImage != null && !eventImage.isEmpty()){
//            Image image = imageService.handleImageUpload(eventImage);
//            event.setEventImage(image);
//        }
//
//        eventsRepository.save(event);
//
//        //notifying hub members of the activity in the background without interrupting the system flow
//        CompletableFuture.runAsync(()->eventsParticipatorsService.notifyChapterMembersAboutANewChapterEvent(event));
//    }
//
//    @Override
//    public void addNewHubEvent(Events event, Long hubId, MultipartFile eventImage) throws NoResourceFoundException, IOException {
//
//    }
//
//
//    @Override
//    public void addNewEvent(Events event, MultipartFile eventImage) throws IOException {
//
//        event.setChapter(null);
//
//        if (eventImage != null && !eventImage.isEmpty()){
//            Image image = imageService.handleImageUpload(eventImage);
//            event.setEventImage(image);
//        }
//
//        eventsRepository.save(event);
//
//        //notifying users of the activity in the background without interrupting the system flow
//        CompletableFuture.runAsync(()->eventsParticipatorsService.notifyUsersAboutANewEvent(event));
//    }
//
//    @Override
//    public CustomResponse<List<Events>> displayAllEvents() throws NoResourceFoundException {
//        CustomResponse<List<Events>> response = new CustomResponse<>();
//
//        try {
//            List<Events> events = eventsRepository.findAll();
//            if (events.isEmpty()) {
//
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
//                response.setPayload(events);
//            } else {
//                response.setStatusCode(HttpStatus.OK.value());
//                response.setMessage(HttpStatus.OK.getReasonPhrase());
//                response.setPayload(events);
//            }
//        } catch (Exception e) {
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setMessage(e.getMessage());
//        }
//        return response;
//    }
//
//
//    @Override
//    public CustomResponse<?> displayEventById(Long eventId) throws NoResourceFoundException {
//        CustomResponse<Events> response = new CustomResponse<>();
//        try {
//            Optional<Events> events = eventsRepository.findById(eventId);
//            Events event = events.get();
//            if (events.isEmpty()) {
//
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
//                response.setPayload(event);
//            } else {
//                response.setStatusCode(HttpStatus.OK.value());
//                response.setMessage(HttpStatus.OK.getReasonPhrase());
//                response.setPayload(event);
//            }
//        } catch (Exception e) {
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setMessage(e.getMessage());
//        }
//        return response;
//
//    }
//
//    @Override
//    public void updateEventById(Long eventId, EventsDto updatedEventDTO, MultipartFile eventImage) throws NoResourceFoundException, IOException {
//        Optional<Events> existingEventOption = eventsRepository.findById(eventId);
//        Events existingEvent = existingEventOption.orElseThrow(() -> new NoResourceFoundException("The event to be updated does not exist"));
//
//        modelMapper.map(updatedEventDTO, existingEvent);
//
//        if (eventImage != null && !eventImage.isEmpty()) {
//            // saving image
//            Image image = imageService.handleImageUpload(eventImage);
//
//            existingEvent.setEventImage(image);
//        }
//
//        eventsRepository.save(existingEvent);
//    }
//
//    @Override
//    public void deleteEventById(Long eventId) throws NoResourceFoundException {
//        Events eventToDelete = eventsRepository.findById(eventId)
//
//                .orElseThrow(() -> new NoResourceFoundException("Event to be deleted not found"));
//        eventsRepository.delete(eventToDelete);
//    }
//
//    @Override
//    public List<Events> displayEventsByHubId(Long chapterId) throws NoResourceFoundException {
//        return null;
//    }
//
//    //END OF CRUD
//
//    @Override
//    public CustomResponse<?> getTotalEvents() {
//        CustomResponse<Long> response =new CustomResponse<>();
//        try {
//            String queryString = "SELECT COUNT(a) FROM Events a";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            Long total= query.getSingleResult();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        return response;
//
//    }
//    @Override
//    public CustomResponse<List<Events>> displayEventsByChapterId(Long chapterId) throws NoResourceFoundException {
//        CustomResponse<List<Events>> response = new CustomResponse<>();
//
//        try {
//            List<Events> events = eventsRepository.findEventsByChapterId(chapterId);
//            if (events.isEmpty()) {
//
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
//                response.setPayload(events);
//            } else {
//                response.setStatusCode(HttpStatus.OK.value());
//                response.setMessage(HttpStatus.OK.getReasonPhrase());
//                response.setPayload(events);
//            }
//        } catch (Exception e) {
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setMessage(e.getMessage());
//        }
//        return response;
//    }
//
//    @Override
//    public void updateEventStatus(Events event) {
//
//    }
//
//
//    @Override
//    public CustomResponse<?> getTotalEventsByChapterId(Long chapterId) {
//        CustomResponse<Long> response = new CustomResponse<>();
//        try {
//            String queryString = "SELECT COUNT(a) FROM Events a WHERE a.chapter.id = :chapterId";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            query.setParameter("chapterId", chapterId);
//            Long total= query.getSingleResult();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        return response;
//    }
//
//    @Override
//    public CustomResponse<?> getTotalEventsByHubId(Long hubId) {
//        return null;
//    }
//
//
//    @Override
//    public CustomResponse<List<Events>> displayEventsWithOutChapter() {
//        CustomResponse<List<Events>>response=new CustomResponse<>();
//        try {
//            String queryString = "SELECT a FROM Events a WHERE a.chapter IS NULL";
//            TypedQuery<Events> query = entityManager.createQuery(queryString, Events.class);
//            var result= query.getResultList();
//            if (result.isEmpty()) {
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            } else {
//                response.setPayload(result);
//                response.setMessage("Successful");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        } catch (Exception e) {
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return response;
//    }
//
//
//
//
//    @Override
//    public CustomResponse<?> getTotalEventsWithOutChapters() {
//        CustomResponse<Long> response = new CustomResponse<>();
//        try{
//            String queryString = "SELECT COUNT(a) FROM Events a WHERE a.chapter IS NULL";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            var total =  query.getSingleResult();
//            if (total==null) {
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            } else {
//                response.setPayload(total);
//                response.setMessage("Successful");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        } catch (Exception e) {
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return response;
//    }
//
//
//    @Override
//    public CustomResponse<List<Events>> displayScheduledEvents() {
//        CustomResponse<List<Events>> response=new  CustomResponse<>();
//        try {
//            String queryString = "SELECT a FROM Events a WHERE a.eventStatus =:status";
//            TypedQuery<Events> query = entityManager.createQuery(queryString, Events.class);
//            query.setParameter("status", EventStatus.SCHEDULED);
//            var total=query.getResultList();
//
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return response;
//    }
//
//    @Override
//    public CustomResponse<?> getTotalScheduledEvents() {
//        CustomResponse<Long> response = new CustomResponse<>();
//        try {
//            String queryString = "SELECT COUNT (a) FROM Events a WHERE a.eventStatus = :status";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            query.setParameter("status", EventStatus.SCHEDULED);
//            Long total= query.getSingleResult();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        return response;
//
//    }
//
//    @Override
//    public CustomResponse<List<Events>> displayPastEvents() {
//        CustomResponse<List<Events>>response=new CustomResponse<>();
//        try {
//            String queryString = "SELECT a FROM Events a WHERE a.eventStatus =:status";
//            TypedQuery<Events> query = entityManager.createQuery(queryString, Events.class);
//            query.setParameter("status", EventStatus.PAST);
//            var result= query.getResultList();
//            if (result.isEmpty()) {
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            } else {
//                response.setPayload(result);
//                response.setMessage("Successful");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        } catch (Exception e) {
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return response;
//    }
//
//
//
//
//    @Override
//    public CustomResponse<?> getTotalPastEvents() {
//        CustomResponse<Long> response= new CustomResponse<>();
//        try {
//            String queryString = "SELECT COUNT (a) FROM Events a WHERE a.eventStatus = :status";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            query.setParameter("status", EventStatus.PAST);
//            Long total= query.getSingleResult();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        return response;
//    }
//
//    @Override
//    public CustomResponse<List<Events>> displayActiveEvents() {
//        CustomResponse<List<Events>>response= new CustomResponse<>();
//        try {
//            String queryString = "SELECT a FROM Events a WHERE a.eventStatus =:status";
//            TypedQuery<Events> query = entityManager.createQuery(queryString, Events.class);
//            query.setParameter("status", EventStatus.ONGOING);
//            var total =query.getResultList();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        return response;
//    }
//
//
//    @Override
//    public CustomResponse<?> getTotalActiveEvents() {
//        CustomResponse<Long> response= new CustomResponse<>();
//        try {
//            String queryString = "SELECT COUNT (a) FROM Events a WHERE a.eventStatus = :status";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            query.setParameter("status", EventStatus.ONGOING);
//            Long total= query.getSingleResult();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        return response;
//    }
//
//
//    @Override
//    public CustomResponse<List<Events>> displayEventsByDate(LocalDate eventDate) {
//        CustomResponse<List<Events>> response= new CustomResponse<>();
//        //convert LocalDate to localDateTime by setting time to midnight
//        LocalDateTime startDateTime = eventDate.atStartOfDay();
//
//        //Calculate the end of the day
//        LocalDateTime endDateTime = eventDate.atTime(LocalTime.MAX);
//
//        try {
//            String queryString = "SELECT a FROM Events a WHERE a.eventDate >= :startDateTime AND a.eventDate <= :endDateTime";
//            TypedQuery<Events> query = entityManager.createQuery(queryString, Events.class);
//            query.setParameter("startDateTime", startDateTime);
//            query.setParameter("endDateTime", endDateTime);
//
//            var total= query.getResultList();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        }
//        finally {
//            entityManager.close();
//        }
//        return response;
//
//    }
//
//    @Override
//    public CustomResponse<?> getTotalEventsByDate(LocalDate eventDate) {
//        CustomResponse<Long> response= new CustomResponse<>();
//        // Convert LocalDate to LocalDateTime by setting time to midnight
//        LocalDateTime startDateTime = eventDate.atStartOfDay();
//
//        // Calculate the end of the day
//        LocalDateTime endDateTime = eventDate.atTime(LocalTime.MAX);
//        try {
//            String queryString = "SELECT COUNT(a) FROM Events a WHERE a.eventDate >= :startDateTime AND a.eventDate <= :endDateTime";
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//            query.setParameter("startDateTime", startDateTime);
//            query.setParameter("endDateTime", endDateTime);
//
//            var total= query.getSingleResult();
//            if(total==null){
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("No event found");
//                response.setPayload(null);
//            }else {
//                response.setPayload(total);
//                response.setMessage("Found");
//                response.setStatusCode(HttpStatus.OK.value());
//            }
//        }catch (Exception e){
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        finally {
//            entityManager.close();
//        }
//        return response;
//
//    }
//
//    public void updateEventStatus(Events event, String eventDateString) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime activityDateTime;
//
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Or use the pattern that matches your input
//            activityDateTime = LocalDateTime.parse(eventDateString, formatter);
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Invalid date format. Please use the correct format (e.g., yyyy-MM-dd'T'HH:mm:ss).");
//        }
//
//        if (activityDateTime.isBefore(now.minusHours(8))) {
//            event.setEventStatus(EventStatus.PAST);
//        } else if (activityDateTime.isAfter(now.plusHours(1))) {
//            event.setEventStatus(EventStatus.SCHEDULED);
//        } else {
//            event.setEventStatus(EventStatus.ONGOING);
//        }
//    }
//}
