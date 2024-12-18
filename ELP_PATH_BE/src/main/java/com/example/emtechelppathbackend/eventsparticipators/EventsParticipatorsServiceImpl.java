package com.example.emtechelppathbackend.eventsparticipators;

import com.example.emtechelppathbackend.attachabledata.ImageAttachableData;
import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMember;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMemberRepository;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMemberService;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.events.Events;
import com.example.emtechelppathbackend.events.EventsRepo;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubmembers.HubMember;
import com.example.emtechelppathbackend.hubmembers.HubMemberRepo;
import com.example.emtechelppathbackend.hubmembers.HubMemberService;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventsParticipatorsServiceImpl implements EventsParticipatorsService{
	  private final EventsRepo eventsRepository;
	  private  final UsersRepository usersRepository;
	  private final ChapterMemberRepository chapterMemberRepository;
	  private final HubMemberRepo hubMemberRepo;
	  private final ChapterMemberService chapterMemberService;
	  private final HubMemberService hubMemberService;
	  private final EventsParticipatorsRepository eventsParticipatorsRepository;
	  private final EmailService emailService;

	  //participating in an event

	  @Transactional
	  public void participateEvent(Long userId, Long eventId, String eventDateString) throws NoResourceFoundException {
		  Users user = usersRepository.findById(userId)
				  .orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found"));

		  Events event = eventsRepository.findById(eventId)
				  .orElseThrow(() -> new NoResourceFoundException("Event with ID " + eventId + " not found"));

		  LocalDateTime eventDate;
		  try {
			  DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // or use a custom pattern if needed
			  eventDate = LocalDateTime.parse(eventDateString, formatter);
		  } catch (DateTimeParseException e) {
			  throw new IllegalArgumentException("Invalid date format for event. Please use ISO_LOCAL_DATE_TIME format.");
		  }

		  if (eventDate.isBefore(LocalDateTime.now())) {
			  throw new NoResourceFoundException("Cannot participate in a past event.");
		  }

		  List<EventsParticipators> existingParticipations = eventsParticipatorsRepository.findByEventAndParticipator(event, user);
		  Optional<EventsParticipators> existingParticipation = existingParticipations.stream().filter(
				  participation -> participation.getParticipator().equals(user) && (participation.isActiveParticipation())
		  ).findFirst();
		  if (existingParticipation.isPresent()){
			  throw new NoResourceFoundException("User is already a participator of this event");
		  }

		  EventsParticipators eventsParticipators = new EventsParticipators();

		  eventsParticipators.setEvent(event);
		  eventsParticipators.setBookingParticipationTime(LocalDateTime.now());
		  eventsParticipators.setParticipator(user);
		  eventsParticipators.setActiveParticipation(true);

		  eventsParticipatorsRepository.save(eventsParticipators);
	  }

	@Override
	public void participateEvent(Long userId, Long eventId) throws NoResourceFoundException {

	}


	//cancelling a planned participation
	@Override
	public void cancelParticipation(Long userId, Long eventId) {
		Users user = usersRepository.findById(userId)
				.orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found."));

		Events event = eventsRepository.findById(eventId)
				.orElseThrow(() -> new NoResourceFoundException("Event with ID " + eventId + " not found."));

		LocalDateTime eventDate;
		try {
			// Assuming event.getEventDate() now returns a String and the format is ISO_LOCAL_DATE_TIME.
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			eventDate = LocalDateTime.parse(event.getEventDate(), formatter);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format for the event.");
		}

		if (eventDate.isBefore(LocalDateTime.now())) {
			throw new NoResourceFoundException("Cannot cancel participation for past events.");
		}

		List<EventsParticipators> existingParticipation = eventsParticipatorsRepository.findByEvent_Id(eventId);

		Optional<EventsParticipators> foundParticipation = existingParticipation.stream()
				.filter(participation -> participation.getParticipator().equals(user)
						&& (participation.getCancellingParticipationTime() == null)).findFirst();

		if (foundParticipation.isEmpty()) {
			throw new NoResourceFoundException("User of email " + user.getUserEmail() + " is not participating in the event " + event.getEventName() + ".");
		}

		EventsParticipators participationToUpdate = foundParticipation.get();
		participationToUpdate.setCancellingParticipationTime(LocalDateTime.now());
		participationToUpdate.setActiveParticipation(false);

		eventsParticipatorsRepository.save(participationToUpdate);
		// Assuming you might not actually need to save the event here unless there are other changes made to it
		// eventsRepository.save(event);
	}


	  public void notifyChapterMembersAboutANewChapterEvent(Events event) {
		    Chapter chapterToUse = event.getChapter();
		    Set<ChapterMember> recipientsMembers = chapterMemberRepository.findByChapter_IdAndActiveMembershipIsTrue(chapterToUse.getId());
		    Set<Users> recipientsSet = chapterMemberService.extractUsersFromChapterMembers(recipientsMembers);
		    List<Users> recipients = new ArrayList<>(recipientsSet);
		    String subject = event.getEventName();
		    String body = emailService.generateEventsNotificationBody(event);
		    ImageAttachableData eventAttachableData = new ImageAttachableData(event.getEventImage());

		    emailService.sendEmailWithAttachmentToRecipients(recipients, subject, body, eventAttachableData);
	  }


	public void notifyHubMembersAboutANewHubEvent(Events event) {
		Hub hubToUse = event.getHub();
		Set<HubMember> recipientsMembers = hubMemberRepo.findByHub_IdAndActiveMembershipIsTrue(hubToUse.getId());
		Set<Users> recipientsSet = hubMemberService.extractUsersFromHubMembers(recipientsMembers);
		List<Users> recipients = new ArrayList<>(recipientsSet);
		String subject = event.getEventName();
		String body = emailService.generateEventsNotificationBody(event);
		ImageAttachableData eventAttachableData = new ImageAttachableData(event.getEventImage());

		emailService.sendEmailWithAttachmentToRecipients(recipients, subject, body, eventAttachableData);
	}


	  public void notifyUsersAboutANewEvent(Events event) {
		    List<Users> recipients = usersRepository.findAll();
		    String subject = event.getEventName();
		    String body = emailService.generateEventsNotificationBody(event);
		    ImageAttachableData eventAttachableData = new ImageAttachableData(event.getEventImage());

		    emailService.sendEmailWithAttachmentToRecipients(recipients, subject, body, eventAttachableData);
	  }

}
