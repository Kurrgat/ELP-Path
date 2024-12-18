package com.example.emtechelppathbackend.activities.activitiesv2;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ActivityStatusSchedulerTestv2 {

    @Mock
    private ActivityServicev2 activityService;

    @Mock
    private ActivityRepositoryv2 activityRepository;

    @InjectMocks
    private ActivityStatusSchedulerv2 scheduler;

    @Test
    public void testUpdateActivityStatuses() {
        // Initialize the mocks
        // MockitoAnnotations.initMocks(this); this is deprecated
        MockitoAnnotations.openMocks(this);

        // Create sample activities
        ActivityV2 activity1 = new ActivityV2();
        activity1.setActivityStatus(ActivityStatusv2.SCHEDULED);

        ActivityV2 activity2 = new ActivityV2();
        activity2.setActivityStatus(ActivityStatusv2.ONGOING);

        List<ActivityV2> activities = new ArrayList<>();
        activities.add(activity1);
        activities.add(activity2);

        // Mock the repository's findAll method
        when(activityRepository.findAll()).thenReturn(activities);

        // Mock the service's updateActivityStatus method
        doNothing().when(activityService).updateActivityStatus(any(ActivityV2.class));

        // Call the scheduler's method
        scheduler.updateActivityStatuses();

        // Verify that the updateActivityStatus method was called for each activity
        verify(activityService, times(2)).updateActivityStatus(any(ActivityV2.class));

        // Verify that the save method was called for each activity
        verify(activityRepository, times(2)).save(any(ActivityV2.class));
    }
}
