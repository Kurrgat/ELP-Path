package com.example.emtechelppathbackend.activityattendees.activityattendeesv2;

import com.example.emtechelppathbackend.activities.activitiesv2.ActivityV2;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ActivityAttendeesRepositoryv2 extends JpaRepository<ActivityAttendeesV2, Long> {
	 List<ActivityAttendeesV2> findByActivityAndAttendee(ActivityV2 activity, Users attendee);

	 Set<ActivityAttendeesV2> findByActivity_Id(Long activityId);
}
