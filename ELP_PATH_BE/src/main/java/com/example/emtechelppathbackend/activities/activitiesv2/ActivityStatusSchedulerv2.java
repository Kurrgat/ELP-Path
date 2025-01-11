package com.example.emtechelppathbackend.activities.activitiesv2;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityStatusSchedulerv2 {

	  private final ActivityServicev2 activityService;
	  private final ActivityRepositoryv2 activityRepository;

	  @Scheduled(fixedRate = 60000)//60000 milliseconds to update every 1 minute
	  public void updateActivityStatuses() {
		    List<ActivityV2> activities = activityRepository.findAll();

		    //filtering only the activities which are not already updated to past
		    List<ActivityV2> activitiesToUpdate = activities.stream()
				.filter(activity -> activity.getActivityStatus() != ActivityStatusv2.PAST)
				.toList();

		    for (ActivityV2 activity : activitiesToUpdate) {
				activityService.updateActivityStatus(activity);
				activityRepository.save(activity);
		    }
	  }
}
