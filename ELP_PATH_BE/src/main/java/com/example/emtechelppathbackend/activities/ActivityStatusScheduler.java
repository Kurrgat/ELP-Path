package com.example.emtechelppathbackend.activities;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityStatusScheduler {

	  private final ActivityService activityService;
	  private final ActivityRepository activityRepository;

	  @Scheduled(fixedRate = 60000)//60000 milliseconds to update every 1 minute
	  public void updateActivityStatuses() {
		    List<Activity> activities = activityRepository.findAll();

		    //filtering only the activities which are not already updated to past
		    List<Activity> activitiesToUpdate = activities.stream()
				.filter(activity -> activity.getActivityStatus() != ActivityStatus.PAST)
				.toList();

		    for (Activity activity : activitiesToUpdate) {
				activityService.updateActivityStatus(activity);
				activityRepository.save(activity);
		    }
	  }
}
