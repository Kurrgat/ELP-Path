package com.example.emtechelppathbackend.eventsparticipators;

import com.example.emtechelppathbackend.events.Events;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import org.springframework.stereotype.Service;

@Service
public interface EventsParticipatorsService {

	  void participateEvent(Long userId, Long eventId) throws NoResourceFoundException;

	  void cancelParticipation(Long userId, Long eventId);

	  void notifyChapterMembersAboutANewChapterEvent(Events event);

	  void notifyHubMembersAboutANewHubEvent(Events event);

	  void notifyUsersAboutANewEvent(Events event);
}
