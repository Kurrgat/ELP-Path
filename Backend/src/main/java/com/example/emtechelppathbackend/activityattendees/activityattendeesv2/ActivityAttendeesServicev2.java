package com.example.emtechelppathbackend.activityattendees.activityattendeesv2;

import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityAttendeesServicev2 {

	  CustomResponse<?> attendActivity(Long userId, Long activityId) throws NoResourceFoundException;

	  CustomResponse<?> cancelAttendance(Long userId, Long activityId);

	  CustomResponse<?> notifyChapterMembersAboutANewActivity(ActivityV2 activity);

	  CustomResponse<?> notifyHubMembersAboutANewActivity(ActivityV2 activity);

	  ActivityAttendeesV2 getActivityAttendees(List<ActivityAttendeesV2> attendances);
}
