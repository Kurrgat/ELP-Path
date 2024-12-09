package com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2;


import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberRepositoryv2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberServicev2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.emails.EmailService;

import com.example.emtechelppathbackend.events.eventsv2.EventsRepov2;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberServicev2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
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

	  //participating in an event
	  @Override
	  @Transactional
	  public void participateEvent(Long userId, Long eventId) throws NoResourceFoundException {
		    Users user = usersRepository.findById(userId)
				.orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found"));

		    EventsV2 event = eventsRepository.findById(eventId)
				.orElseThrow(() -> new NoResourceFoundException("Event with ID " + eventId + " not found"));

		    if (event.getEventDate().isBefore(LocalDateTime.now())) {
				throw new NoResourceFoundException("Cannot participate in a past event.");
		    }

		    List<EventsParticipatorsv2> existingParticipations = eventsParticipatorsRepository.findByEventAndParticipator(event, user);
			Optional<EventsParticipatorsv2> existingParticipation = existingParticipations.stream().filter(
					participation -> participation.getParticipator().equals(user) && (participation.isActiveParticipation())
			).findFirst();
		    if (existingParticipation.isPresent()){
				throw new NoResourceFoundException("User is already a participator of this event");
		    }

		    EventsParticipatorsv2 eventsParticipators = new EventsParticipatorsv2();

		    eventsParticipators.setEvent(event);
		    eventsParticipators.setBookingParticipationTime(LocalDateTime.now());
		    eventsParticipators.setParticipator(user);
			eventsParticipators.setActiveParticipation(true);

		    eventsParticipatorsRepository.save(eventsParticipators);

		    eventsRepository.save(event);
	  }

	  //cancelling a planned participation
	  @Override
	  public void cancelParticipation(Long userId, Long eventId) {
		    Users user = usersRepository.findById(userId)
				.orElseThrow(() -> new NoResourceFoundException("User with ID " + userId + " not found."));

		    EventsV2 event = eventsRepository.findById(eventId)
				.orElseThrow(() -> new NoResourceFoundException("Event with ID " + eventId + " not found."));

		    if (event.getEventDate().isBefore(LocalDateTime.now())) {
				throw new NoResourceFoundException("Cannot cancel participation for past events.");
		    }

		    List<EventsParticipatorsv2> existingParticipation = eventsParticipatorsRepository.findByEvent_Id(eventId);

		    Optional<EventsParticipatorsv2> foundParticipation = existingParticipation.stream()
				.filter(participation -> participation.getParticipator().equals(user)
					  && (participation.getCancellingParticipationTime() == null)).findFirst();

		    if (foundParticipation.isEmpty()) {
				throw new NoResourceFoundException("User of email " + user.getUserEmail() + " is not participating in the event " + event.getEventName() + ".");
		    }

		    EventsParticipatorsv2 participationToUpdate = foundParticipation.get();
		    participationToUpdate.setCancellingParticipationTime(LocalDateTime.now());
			participationToUpdate.setActiveParticipation(false);

		    eventsParticipatorsRepository.save(participationToUpdate);
		    eventsRepository.save(event);
	  }

	  @Override
	  public void notifyChapterMembersAboutANewChapterEvent(EventsV2 event) {
		    ChapterV2 chapterToUse = event.getChapter();
		    Set<ChapterMemberV2> recipientsMembers = chapterMemberRepository.findByChapter_IdAndActiveMembershipIsTrue(chapterToUse.getId());
		    Set<Users> recipientsSet = chapterMemberService.extractUsersFromChapterMembers(recipientsMembers);
		    List<Users> recipients = new ArrayList<>(recipientsSet);
		    String subject = event.getEventName();
		    String body = emailService.generateEventsNotificationBodyv2(event);

		    emailService.sendEmailWithAttachmentToRecipientsv2(recipients, subject, body, event.getEventImages());
	  }

	@Override
	public void notifyHubMembersAboutANewHubEvent(EventsV2 event) {
		Hubv2 hubToUse = event.getHub();
		Set<HubMemberv2> recipientsMembers = hubMemberRepo.findByHub_IdAndActiveMembershipIsTrue(hubToUse.getId());
		Set<Users> recipientsSet = hubMemberService.extractUsersFromHubMembers(recipientsMembers);
		List<Users> recipients = new ArrayList<>(recipientsSet);
		String subject = event.getEventName();
		String body = emailService.generateEventsNotificationBodyv2(event);
		emailService.sendEmailWithAttachmentToRecipientsv2(recipients, subject, body, event.getEventImages());
	}

	  @Override
	  public void notifyUsersAboutANewEvent(EventsV2 event) {
		    List<Users> recipients = usersRepository.findAll();
		    String subject = event.getEventName();
		    String body = emailService.generateEventsNotificationBodyv2(event);

		    emailService.sendEmailWithAttachmentToRecipientsv2(recipients, subject, body, event.getEventImages());
	  }

}
