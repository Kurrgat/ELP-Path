package com.example.emtechelppathbackend.activityattendees;

import com.example.emtechelppathbackend.activities.Activity;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityAttendeesService {

	  void attendActivity(Long userId, Long activityId) throws NoResourceFoundException;

	  void cancelAttendance(Long userId, Long activityId);

	  void notifyChapterMembersAboutANewActivity(Activity activity);

	  void notifyHubMembersAboutANewActivity(Activity activity);

	  ActivityAttendees getActivityAttendees(List<ActivityAttendees> attendances);
}
