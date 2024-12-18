package com.example.emtechelppathbackend.activities.activitiesv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface ActivityServicev2 {
	  CustomResponse<?> addNewChapterActivity(ActivityV2 activity,Long activityTypeId, Long chapterId, MultipartFile activityImage) throws NoResourceFoundException, IOException;

	  CustomResponse<?> addNewHubActivity(ActivityV2 activity,Long activityTypeId, Long hubId, MultipartFile activityImage) throws NoResourceFoundException, IOException;

	  CustomResponse<List<ActivityV2>> displayAllActivities() throws NoResourceFoundException;

	  CustomResponse<?> getTotalActivities();

	  CustomResponse<ActivityDTOViewv2> displayActivityById(Long activityId) throws NoResourceFoundException;

	  CustomResponse<ActivityDTOv2> updateActivityById(Long activityId, ActivityDTOv2 updatedActivityDTO, MultipartFile activityImage) throws NoResourceFoundException, IOException;

	  CustomResponse<?> deleteActivityById(Long activityId) throws NoResourceFoundException;

	  CustomResponse<List<ActivityV2>> displayActivityByChapterId(Long chapterId) throws NoResourceFoundException;
	  CustomResponse<List<ActivityV2>> displayActivityByHubId(Long hubId) throws NoResourceFoundException;

	  CustomResponse<?> getTotalActivitiesByChapterId(Long chapterId);

	  CustomResponse<?> getTotalActivitiesByHubId(Long chapterId);

	  CustomResponse<List<ActivityV2>> displayScheduledActivities();

	  CustomResponse<?> getTotalScheduledActivities();

	  CustomResponse<List<ActivityV2>> displayPastActivities();

	  CustomResponse<?> getTotalPastActivities();

	  CustomResponse<List<ActivityV2>> displayActiveActivities();

	  CustomResponse<?> getTotalActiveActivities();

	  CustomResponse<List<ActivityV2>> displayActivitiesByDate(LocalDate activityDate);

	  CustomResponse<?> getTotalActivitiesByDate(LocalDate activityDate);

	  CustomResponse<?> updateActivityStatus(ActivityV2 activity);
}

