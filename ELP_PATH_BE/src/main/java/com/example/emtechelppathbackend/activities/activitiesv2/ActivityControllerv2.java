package com.example.emtechelppathbackend.activities.activitiesv2;

import com.example.emtechelppathbackend.activityattendees.activityattendeesv2.ActivityAttendeesServicev2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/activities")
public class ActivityControllerv2 {

    private final ActivityServicev2 activityService;
    private final ActivityAttendeesServicev2 activityAttendeesService;
    private final ModelMapper modelMapper;

    @PostMapping("/{chapterId}/add-new-chapter-activity/{activityTypeId}")
    public ResponseEntity<?> addChapterActivity
            (@ModelAttribute @Valid ActivityDTOv2 activityDTO,
             @RequestParam("activityImage") MultipartFile activityImage, @PathVariable Long chapterId, @PathVariable Long activityTypeId) {
        try {
            ActivityV2 activity = modelMapper.map(activityDTO, ActivityV2.class);
            var result = activityService.addNewChapterActivity(activity,activityTypeId, chapterId, activityImage);

            return ResponseEntity.status(result.getStatusCode()).body(result);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while processing the image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{hubId}/add-new-hub-activity{activityTypeId}")
    public ResponseEntity<?> addHubActivity
            (@ModelAttribute @Valid ActivityDTOv2 activityDTO,
             @RequestParam("activityImage") MultipartFile activityImage, @PathVariable Long hubId,@PathVariable Long activityTypeId) {
        try {
            ActivityV2 activity = modelMapper.map(activityDTO, ActivityV2.class);
            var response = activityService.addNewHubActivity(activity, hubId, activityTypeId,activityImage);

            return ResponseEntity.status(response.getStatusCode()).body(response);
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
            var response = activityAttendeesService.attendActivity(userId, activityId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/cancel-attendance/{activityId}/")
    public ResponseEntity<?> cancelAttendance
            (@PathVariable Long userId, @PathVariable Long activityId) {
        try {
            var response = activityAttendeesService.cancelAttendance(userId, activityId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> displayAllActivities() {
        try {
        var activities = activityService.displayAllActivities();
            return ResponseEntity.status(activities.getStatusCode()).body(activities);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{activityId}/display")
    public ResponseEntity<?> displayActivityById(@PathVariable Long activityId) {
        try {
            var activity = activityService.displayActivityById(activityId);
            return ResponseEntity.status(activity.getStatusCode()).body(activity);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{chapterId}/display-chapter-activities")
    public ResponseEntity<?> displayActivitiesByChapterId(@PathVariable Long chapterId) {
        try {
            var response = activityService.displayActivityByChapterId(chapterId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{hubId}/display-hub-activities")
    public ResponseEntity<?> displayActivitiesByHubId(@PathVariable Long hubId) {
        try {
            var activities = activityService.displayActivityByHubId(hubId);
            return ResponseEntity.status(activities.getStatusCode()).body(activities);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count-all-activities")
    public ResponseEntity<?> getTotalActivities() {
        var response = activityService.getTotalActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{chapterId}/count-chapter-activities")
    public ResponseEntity<?> getTotalActivitiesByChapterId(@PathVariable Long chapterId) {
        var response = activityService.getTotalActivitiesByChapterId(chapterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{hubId}/count-hub-activities")
    public ResponseEntity<?> getTotalActivitiesByHubId(@PathVariable Long hubId) {
        var response = activityService.getTotalActivitiesByHubId(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/display-scheduled-activities")
    public ResponseEntity<?> displayScheduledActivities() {
        var response = activityService.displayScheduledActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-scheduled-activities")
    public ResponseEntity<?> getTotalScheduledActivities() {
        var response = activityService.getTotalScheduledActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/display-past-activities")
    public ResponseEntity<?> displayPastActivities() {
        var response = activityService.displayPastActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-past-activities")
    public ResponseEntity<?> getTotalPastActivities() {
        var response = activityService.getTotalPastActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/display-active-activities")
    public ResponseEntity<?> displayActiveActivities() {
        var response = activityService.displayActiveActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("count-active-activities")
    public ResponseEntity<?> getTotalActiveActivities() {
        var response = activityService.getTotalActiveActivities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{activityDate}/display-activities-by-date")
    public ResponseEntity<?> displayActivitiesByDate(@PathVariable LocalDate activityDate) {
        var response = activityService.displayActivitiesByDate(activityDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{activityDate}/count-activities-by-date")
    public ResponseEntity<?> getTotalActivitiesByDate(@PathVariable LocalDate activityDate) {
        var response = activityService.getTotalActivitiesByDate(activityDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{activityId}/update")
    public ResponseEntity<?> updateActivityById(
            @ModelAttribute @Valid ActivityDTOv2 updatedActivityDTO,
            @PathVariable Long activityId,
            @RequestParam(value = "activityImage", required = false) MultipartFile activityImage) {
        try {
            var updatedActivity = activityService.updateActivityById(activityId, updatedActivityDTO, activityImage);
            return ResponseEntity.status(updatedActivity.getStatusCode()).body(updatedActivity);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error while processing the image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{activityId}/delete")
    public ResponseEntity<?> deleteActivityById(@PathVariable Long activityId) {
        try {
            var response = activityService.deleteActivityById(activityId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}