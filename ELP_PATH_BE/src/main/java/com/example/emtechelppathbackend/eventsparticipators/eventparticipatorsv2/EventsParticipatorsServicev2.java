package com.example.emtechelppathbackend.eventsparticipators.eventparticipatorsv2;

import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface EventsParticipatorsServicev2 {

	  void participateEvent(Long userId, Long eventId) throws NoResourceFoundException;

	  CustomResponse<?> cancelParticipation(Long userId, Long eventId);

	  void notifyChapterMembersAboutANewChapterEvent(EventsV2 event);

	  void notifyHubMembersAboutANewHubEvent(EventsV2 event);

	  void notifyUsersAboutANewEvent(EventsV2 event);


}
