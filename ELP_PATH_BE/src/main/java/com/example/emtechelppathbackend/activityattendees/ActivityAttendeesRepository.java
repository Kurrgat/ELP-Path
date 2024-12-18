package com.example.emtechelppathbackend.activityattendees;

import com.example.emtechelppathbackend.activities.Activity;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ActivityAttendeesRepository extends JpaRepository<ActivityAttendees, Long> {
	 List<ActivityAttendees> findByActivityAndAttendee(Activity activity, Users attendee);

	 Set<ActivityAttendees> findByActivity_Id(Long activityId);
}
