package com.example.emtechelppathbackend.activities;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ActivityStatusSchedulerTest {

    @Mock
    private ActivityService activityService;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityStatusScheduler scheduler;

    @Test
    public void testUpdateActivityStatuses() {
        // Initialize the mocks
        // MockitoAnnotations.initMocks(this); no longer in use
        MockitoAnnotations.openMocks(this);

        // Create sample activities
        Activity activity1 = new Activity();
        activity1.setActivityStatus(ActivityStatus.SCHEDULED);

        Activity activity2 = new Activity();
        activity2.setActivityStatus(ActivityStatus.ONGOING);

        List<Activity> activities = new ArrayList<>();
        activities.add(activity1);
        activities.add(activity2);

        // Mock the repository's findAll method
        when(activityRepository.findAll()).thenReturn(activities);

        // Mock the service's updateActivityStatus method
        doNothing().when(activityService).updateActivityStatus(any(Activity.class));

        // Call the scheduler's method
        scheduler.updateActivityStatuses();

        // Verify that the updateActivityStatus method was called for each activity
        verify(activityService, times(2)).updateActivityStatus(any(Activity.class));

        // Verify that the save method was called for each activity
        verify(activityRepository, times(2)).save(any(Activity.class));
    }
}
