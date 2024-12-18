package com.example.emtechelppathbackend.activities;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface ActivityService {
	  void addNewChapterActivity(Activity activity, Long chapterId, MultipartFile activityImage) throws NoResourceFoundException, IOException;

	  void addNewHubActivity(Activity activity, Long hubId, MultipartFile activityImage) throws NoResourceFoundException, IOException;

	  List<Activity> displayAllActivities() throws NoResourceFoundException;

	  long getTotalActivities();

	  Activity displayActivityById(Long activityId) throws NoResourceFoundException;

	  Activity updateActivityById(Long activityId, ActivityDTO updatedActivityDTO, MultipartFile activityImage) throws NoResourceFoundException, IOException;

	  void deleteActivityById(Long activityId) throws NoResourceFoundException;

	  List<Activity> displayActivityByChapterId(Long chapterId) throws NoResourceFoundException;
	  List<Activity> displayActivityByHubId(Long hubId) throws NoResourceFoundException;

	  long getTotalActivitiesByChapterId(Long chapterId);

	  long getTotalActivitiesByHubId(Long chapterId);

	  List<Activity> displayScheduledActivities();

	  long getTotalScheduledActivities();

	  List<Activity> displayPastActivities();

	  long getTotalPastActivities();

	  List<Activity> displayActiveActivities();

	  long getTotalActiveActivities();

	  List<Activity> displayActivitiesByDate(LocalDate activityDate);

	  long getTotalActivitiesByDate(LocalDate activityDate);

	  void updateActivityStatus(Activity activity);
}

