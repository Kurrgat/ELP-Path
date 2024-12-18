package com.example.emtechelppathbackend.activitytypes.activitytypesv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/activity-types")
public class ActivityTypeControllerv2 {

	  private final ActivityTypeServicev2 activityTypeService;

	  @PostMapping("/add-new-activityType")
	  public ResponseEntity<?> addActivityType(@RequestBody @Valid ActivityTypeDTOv2 activityTypeDTO) {
		    try {
				var response = activityTypeService.addNewActivityType(activityTypeDTO);
				return ResponseEntity.status(response.getStatusCode()).body(response);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }

	  }

	  @GetMapping("/all")
	  public ResponseEntity<?> displayAllActivityTypes() {
		    try {
				var response = activityTypeService.displayAllActivityTypes();
				return ResponseEntity.status(response.getStatusCode()).body(response);
		    } catch (NoResourceFoundException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }

	  @GetMapping("{activityTypeId}/display")
	  public ResponseEntity<?> diplayActivityTypeById(@PathVariable Long activityTypeId) {
		    try {
				var response = activityTypeService.displayActivityTypeById(activityTypeId);
				return ResponseEntity.status(response.getStatusCode()).body(response);
		    } catch (NoResourceFoundException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }

	  @PutMapping("/{activityTypeId}/update")
	  public ResponseEntity<?> updateActivityTypeById(@RequestBody ActivityTypeDTOv2 updatedActivityTypeDTO, @PathVariable Long activityTypeId) {
		    try {
				var response = activityTypeService.updateActivityTypeById(updatedActivityTypeDTO, activityTypeId);
				return ResponseEntity.status(response.getStatusCode()).body(response);
		    } catch (NoResourceFoundException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }

	  @DeleteMapping("/{activityTypeId}/delete")
	  public ResponseEntity<?> deleteActivityTypeById(@PathVariable Long activityTypeId) {
		    try {
				var response = activityTypeService.deleteActivityTypeById(activityTypeId);
				return ResponseEntity.status(response.getStatusCode()).body(response);
		    } catch (NoResourceFoundException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }
}
