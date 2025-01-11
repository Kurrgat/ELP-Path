package com.example.emtechelppathbackend.activities;

import com.example.emtechelppathbackend.activityattendees.ActivityAttendeesService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final ActivityAttendeesService activityAttendeesService;
    private final ModelMapper modelMapper;

    @PostMapping("/{chapterId}/add-new-chapter-activity")
    public ResponseEntity<?> addChapterActivity
            (@ModelAttribute @Valid ActivityDTO activityDTO,
             @RequestParam("activityImage") MultipartFile activityImage, @PathVariable Long chapterId) {
        try {
            Activity activity = modelMapper.map(activityDTO, Activity.class);
            activityService.addNewChapterActivity(activity, chapterId, activityImage);

            return ResponseEntity.ok(new ResponseRecordOFMessages("Activity created Successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while processing the image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{hubId}/add-new-hub-activity")
    public ResponseEntity<?> addHubActivity
            (@ModelAttribute @Valid ActivityDTO activityDTO,
             @RequestParam("activityImage") MultipartFile activityImage, @PathVariable Long hubId) {
        try {
            Activity activity = modelMapper.map(activityDTO, Activity.class);
            activityService.addNewHubActivity(activity, hubId, activityImage);

            return ResponseEntity.ok(new ResponseRecordOFMessages("Activity created Successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while processing the image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/attend-activity/{activityId}/")
    public ResponseEntity<?> attendActivity
            (@PathVariable Long userId, @PathVariable Long activityId) {
        try {
            activityAttendeesService.attendActivity(userId, activityId);
            return ResponseEntity.ok(new ResponseRecordOFMessages("Attendance recorded successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/cancel-attendance/{activityId}/")
    public ResponseEntity<?> cancelAttendance
            (@PathVariable Long userId, @PathVariable Long activityId) {
        try {
            activityAttendeesService.cancelAttendance(userId, activityId);
            return ResponseEntity.ok("Activity attendance cancelled successfully");
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> displayAllActivities() {
        try {
            List<Activity> activities = activityService.displayAllActivities();
            return ResponseEntity.ok(activities);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{activityId}/display")
    public ResponseEntity<?> displayActivityById(@PathVariable Long activityId) {
        try {
            Activity activity = activityService.displayActivityById(activityId);
            ActivityDTOView responseDTO = modelMapper.map(activity, ActivityDTOView.class);
            return ResponseEntity.ok(responseDTO);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{chapterId}/display-chapter-activities")
    public ResponseEntity<?> displayActivitiesByChapterId(@PathVariable Long chapterId) {
        try {
            List<Activity> activities = activityService.displayActivityByChapterId(chapterId);
            return ResponseEntity.ok(activities);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{hubId}/display-hub-activities")
    public ResponseEntity<?> displayActivitiesByHubId(@PathVariable Long hubId) {
        try {
            List<Activity> activities = activityService.displayActivityByHubId(hubId);
            return ResponseEntity.ok(activities);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count-all-activities")
    public long getTotalActivities() {
        return activityService.getTotalActivities();
    }

    @GetMapping("/{chapterId}/count-chapter-activities")
    public long getTotalActivitiesByChapterId(@PathVariable Long chapterId) {
        return activityService.getTotalActivitiesByChapterId(chapterId);
    }

    @GetMapping("/{hubId}/count-hub-activities")
    public long getTotalActivitiesByHubId(@PathVariable Long hubId) {
        return activityService.getTotalActivitiesByHubId(hubId);
    }

    @GetMapping("/display-scheduled-activities")
    public List<Activity> displayScheduledActivities() {
        return activityService.displayScheduledActivities();
    }

    @GetMapping("/count-scheduled-activities")
    public long getTotalScheduledActivities() {
        return activityService.getTotalScheduledActivities();
    }

    @GetMapping("/display-past-activities")
    public List<Activity> displayPastActivities() {
        return activityService.displayPastActivities();
    }

    @GetMapping("/count-past-activities")
    public long getTotalPastActivities() {
        return activityService.getTotalPastActivities();
    }

    @GetMapping("/display-active-activities")
    public List<Activity> displayActiveActivities() {
        return activityService.displayActiveActivities();
    }

    @GetMapping("count-active-activities")
    public long getTotalActiveActivities() {
        return activityService.getTotalActiveActivities();
    }

    @GetMapping("/{activityDate}/display-activities-by-date")
    public List<Activity> displayActivitiesByDate(@PathVariable LocalDate activityDate) {
        return activityService.displayActivitiesByDate(activityDate);
    }

    @GetMapping("/{activityDate}/count-activities-by-date")
    public long getTotalActivitiesByDate(@PathVariable LocalDate activityDate) {
        return activityService.getTotalActivitiesByDate(activityDate);
    }

    @PutMapping("/{activityId}/update")
    public ResponseEntity<?> updateActivityById(
            @ModelAttribute @Valid ActivityDTO updatedActivityDTO,
            @PathVariable Long activityId,
            @RequestParam(value = "activityImage", required = false) MultipartFile activityImage) {
        try {
            Activity updatedActivity = activityService.updateActivityById(activityId, updatedActivityDTO, activityImage);
            ActivityDTO responseDTO = modelMapper.map(updatedActivity, ActivityDTO.class);
            return ResponseEntity.ok(responseDTO);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while processing the image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{activityId}/delete")
    public ResponseEntity<?> deleteActivityById(@PathVariable Long activityId) {
        try {
            activityService.deleteActivityById(activityId);
            return ResponseEntity.ok("Activity removed successfully");
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}