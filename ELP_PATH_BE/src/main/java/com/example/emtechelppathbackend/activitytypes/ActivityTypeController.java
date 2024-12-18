package com.example.emtechelppathbackend.activitytypes;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity-types")
public class ActivityTypeController {

	  private final ActivityTypeService activityTypeService;
	  private final ModelMapper modelMapper;

	  @PostMapping("/add-new-activityType")
	  public ResponseEntity<?> addActivityType(@RequestBody @Valid ActivityTypeDTO activityTypeDTO) {
		    try {
				ActivityType activityType = modelMapper.map(activityTypeDTO, ActivityType.class);
				ActivityType response = activityTypeService.addNewActivityType(activityType);
				return ResponseEntity.ok(new ResponseRecord("ActivityType created successfully", response));
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }

	  }

	  @GetMapping("/all")
	  public ResponseEntity<?> displayAllActivityTypes() {

		var activities = activityTypeService.displayAllActivityTypes();

				return  ResponseEntity.status(activities.getStatusCode()).body(activities);

	  }

	  @GetMapping("{activityTypeId}/display")
	  public ResponseEntity<?> diplayActivityTypeById(@PathVariable Long activityTypeId) {
		   var response=activityTypeService.displayActivityTypeById(activityTypeId);
				return ResponseEntity.status(response.getStatusCode()).body(response);

	  }

	  @PutMapping("/{activityTypeId}/update")
	  public ResponseEntity<?> updateActivityTypeById(@RequestBody ActivityTypeDTO updatedActivityTypeDTO, @PathVariable Long activityTypeId) {
		    try {
				ActivityType updatedActivityType = activityTypeService.updateActivityTypeById(updatedActivityTypeDTO, activityTypeId);
				ActivityTypeDTO responseDTO = modelMapper.map(updatedActivityType, ActivityTypeDTO.class);
				return ResponseEntity.ok(new ResponseRecord("updated successfully", responseDTO));
		    } catch (NoResourceFoundException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }

	  @DeleteMapping("/{activityTypeId}/delete")
	  public ResponseEntity<?> deleteActivityTypeById(@PathVariable Long activityTypeId) {
		    try {
				activityTypeService.deleteActivityTypeById(activityTypeId);
				return ResponseEntity.ok(new ResponseRecord("ActivityType removed successfully", null));
		    } catch (NoResourceFoundException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }
}
