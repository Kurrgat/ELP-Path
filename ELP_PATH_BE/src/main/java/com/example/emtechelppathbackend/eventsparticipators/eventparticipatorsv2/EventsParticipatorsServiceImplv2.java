package com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2;


import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberRepositoryv2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberServicev2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;

import com.example.emtechelppathbackend.events.eventsv2.EventsRepov2;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberServicev2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.notification.Notification;
import com.example.emtechelppathbackend.notification.NotificationRepository;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventsParticipatorsServiceImplv2 implements EventsParticipatorsServicev2{
	  private final EventsRepov2 eventsRepository;
	  private  final UsersRepository usersRepository;
	  private final ChapterMemberRepositoryv2 chapterMemberRepository;
	  private final HubMemberRepov2 hubMemberRepo;
	  private final ChapterMemberServicev2 chapterMemberService;
	  private final HubMemberServicev2 hubMemberService;
	  private final EventsParticipatorsRepositoryv2 eventsParticipatorsRepository;
	  private final EmailService emailService;
	  private final NotificationRepository notificationRepository;
	  private final EventsRepov2 eventsRepo;

	  //participating in an event
	  @Override
	  @Transactional
	  public void participateEvent(Long userId, Long eventId) throws NoResourceFoundException {
		  Users user = usersRepository.findById(userId)
				  .orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found"));

		  EventsV2 event = eventsRepository.findById(eventId)
				  .orElseThrow(() -> new NoResourceFoundException("Event with ID " + eventId + " not found"));

		  LocalDateTime eventDate = event.getEventDate(); // Accessing event date directly

		  if (eventDate.isBefore(LocalDateTime.now())) {
			  throw new NoResourceFoundException("Cannot participate in a past event.");
		  }

		  List<EventsParticipatorsv2> existingParticipations = eventsParticipatorsRepository.findByEventAndParticipator(event, user);
		  Optional<EventsParticipatorsv2> existingParticipation = existingParticipations.stream().filter(
				  participation -> participation.getParticipator().equals(user) && participation.isActiveParticipation()
		  ).findFirst();
		  if (existingParticipation.isPresent()) {
			  throw new NoResourceFoundException("User is already a participator of this event");
		  }

		  EventsParticipatorsv2 eventsParticipators = new EventsParticipatorsv2();

		  eventsParticipators.setEvent(event);
		  eventsParticipators.setBookingParticipationTime(LocalDateTime.now());
		  eventsParticipators.setParticipator(user);
		  eventsParticipators.setActiveParticipation(true);

		  eventsParticipatorsRepository.save(eventsParticipators);
	  }




	@Override
	public CustomResponse<?> cancelParticipation(Long userId, Long eventId) {
		CustomResponse<Void> response = new CustomResponse<>();

		try {
			// Check if the user exists
			Users user = usersRepository.findById(userId)
					.orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found."));

			// Check if the event exists
			EventsV2 event = eventsRepository.findById(eventId)
					.orElseThrow(() -> new NoResourceFoundException("Event with ID " + eventId + " not found."));

			// Ensure the event is in the future
			LocalDateTime eventDate = event.getEventDate();
			if (eventDate.isBefore(LocalDateTime.now())) {
				throw new NoResourceFoundException("Cannot cancel participation for past events.");
			}

			// Find the participation record
			List<EventsParticipatorsv2> existingParticipation = eventsParticipatorsRepository.findByEventAndParticipator(event, user);

			EventsParticipatorsv2 participation = existingParticipation.stream()
					.filter(p -> p.getCancellingParticipationTime() == null)
					.findFirst()
					.orElseThrow(() -> new NoResourceFoundException("Participation not found or already cancelled for user with email " + user.getUserEmail() + " in the event " + event.getEventName() + "."));

			// Delete the participation record
			eventsParticipatorsRepository.delete(participation);

			response.setMessage("Participation cancelled successfully.");
			response.setSuccess(true);
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(null);
		} catch (Exception e) {
			response.setMessage("Error: " + e.getMessage());
			response.setSuccess(false);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setPayload(null);
		}

		return response;
	}

	private LocalDateTime parseEventDate(String eventDateString) {
		// Define a DateTimeFormatter pattern to match the format of the date string
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.]");

		// Parse the event date string to LocalDateTime
		return LocalDateTime.parse(eventDateString.trim(), formatter);
	}



	public void notifyChapterMembersAboutANewChapterEvent(EventsV2 event) {
		System.out.println("Debugging stage ");
		ChapterV2 chapterToUse = event.getChapter();
		System.out.println("chapter to use:" +chapterToUse);

		List<ChapterMemberRepositoryv2.ChapterMembership> recipients= chapterMemberRepository.findByChapterMembers(chapterToUse.getId());
		System.out.println(recipients+"receivers");
		// Create and save notifications for each user
		for (ChapterMemberRepositoryv2.ChapterMembership recipient : recipients) {
			try {
				Optional<Users> user = usersRepository.getUserById(recipient.getUserId());

				if (user.isPresent()) {
					saveNotificationForUser(event, user.get());
					sendEmailNotification(event, user.get());
				}
			} catch (Exception e) {
				System.out.println(("error"+e.getMessage()));
			}
		}

	  }

	public void sendEmailNotification(EventsV2 event, Users user) {
		System.out.println("My hub event email");

		String subject = "New Event Notification";
		String message = "Dear " + user.getUserEmail() + ",\n\n"
				+ "A new event \"" + event.getEventName() + "\" has been added to your chapter. "
				+ "Please check the details.\n\n"
				+ "Regards,\n"
				+ "Your Chapter Team";

		// Create an EmailDetails object with the message and subject
		EmailDetails emailDetails = new EmailDetails();
		emailDetails.setRecipient(user.getEmail());
		emailDetails.setMessageBody(message);
		emailDetails.setSubject(subject);

		try {
			// Call the service method to send the email without attachment
			String response = emailService.sendWithoutAttachment(emailDetails);
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}


	private void saveNotificationForUser(EventsV2 event, Users user) {
		String message = "Check New Event : " + event.getEventName(); // Customize this message as needed
		Notification notification = new Notification();

		notification.setMessage(message);
		notification.setUsers(user);
		notification.setEvents(event);
		notificationRepository.save(notification);

	}


	@Override
	public void notifyHubMembersAboutANewHubEvent(EventsV2 event) {
		Hubv2 hubToUse = event.getHub();

		List<HubMemberRepov2.HubMembership> recipients = hubMemberRepo.findHubMembersByHubId(hubToUse.getId());
		for (HubMemberRepov2.HubMembership recipient : recipients) {
				try {
					Optional<Users> user = usersRepository.getUserById(recipient.getUserId());

					if (user.isPresent()) {
						saveNotificationForUser(event, user.get());
						sendEmailNotification(event, user.get());
					}
				} catch (Exception e) {
					System.out.println(("error"+e.getMessage()));
				}
			}
	}




	public void notifyUsersAboutANewEvent(EventsV2 event) {
		EventsV2 savedEvent = eventsRepo.save(event);

		List<Users> recipients = usersRepository.findAll();

		for (Users recipient : recipients) {
			saveNotificationForUser(savedEvent, recipient);
			sendEmailNotification(event, recipient);
		}
	}



}
