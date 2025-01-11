package com.example.emtechelppathbackend.activitytypes.activitytypesv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityTypeServiceImplv2 implements ActivityTypeServicev2 {
	  private final ActivityTypeRepositoryv2 activityTypeRepository;
	  private final ModelMapper modelMapper;

	  @Autowired
	  ServerProperties serverProperties;

	  @Override
	  public CustomResponse<ActivityTypeV2> addNewActivityType(ActivityTypeDTOv2 activityTypeDto) {
		  CustomResponse<ActivityTypeV2> response = new CustomResponse<>();
		  try {
			  ActivityTypeV2 activityType = modelMapper.map(activityTypeDto, ActivityTypeV2.class);
			  activityTypeRepository.save(activityType);


			  response.setMessage("activity saved successfully");
			  response.setStatusCode(HttpStatus.OK.value());
			  response.setPayload(activityType);
		  } catch (Exception e) {
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			  response.setSuccess(false);
		  }
		  return response;
	  }

	  @Override
	  public CustomResponse<List<ActivityTypeV2>> displayAllActivityTypes() throws NoResourceFoundException {
		  CustomResponse<List<ActivityTypeV2>> response = new CustomResponse<>();

		  try {
			  List<ActivityTypeV2> activities = activityTypeRepository.findAll();

			  if ((activities.isEmpty())) {
				  response.setMessage("No activity types found");
				  response.setStatusCode(HttpStatus.NOT_EXTENDED.value());
				  response.setSuccess(false);
				  response.setPayload(activities);
			  } else {
				  response.setMessage("Found "+activities.size()+" activity types");
				  response.setStatusCode(HttpStatus.OK.value());
				  response.setPayload(activities);
			  }
		  } catch (Exception e) {
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			  response.setSuccess(false);
		  }
		  return response;
	  }

	  @Override
	  public CustomResponse<ActivityTypeV2> displayActivityTypeById(Long activityTypeId) throws NoResourceFoundException {
		  CustomResponse<ActivityTypeV2> response = new CustomResponse<>();

		  try {
			  Optional<ActivityTypeV2> optional = activityTypeRepository.findById(activityTypeId);

			  if(optional.isEmpty()) {
				  response.setMessage("Activity type with id "+activityTypeId+" does  not exist");
				  response.setStatusCode(HttpStatus.NOT_EXTENDED.value());
				  response.setSuccess(false);
			  } else {
				  ActivityTypeV2 activityTypev2 = optional.get();

				  response.setMessage("Activity type found");
				  response.setStatusCode(HttpStatus.OK.value());
				  response.setPayload(activityTypev2);
			  }
		  } catch(Exception e) {
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			  response.setSuccess(false);
		  }
		  return response;
	  }

	  @Override
	  public CustomResponse<ActivityTypeV2> updateActivityTypeById(ActivityTypeDTOv2 updatedActivityTypeDTO, Long activityTypeId) throws NoResourceFoundException {
		  CustomResponse<ActivityTypeV2> response = new CustomResponse<>();
		    try {
				Optional<ActivityTypeV2> existingActivityTypeOption = activityTypeRepository.findById(activityTypeId);

				if(existingActivityTypeOption.isEmpty()) {
					response.setMessage("The ActivityType to be updated does not exist");
					response.setStatusCode(HttpStatus.NOT_FOUND.value());
					response.setSuccess(false);
				} else {
					ActivityTypeV2 existingActivityType = existingActivityTypeOption.get();
					modelMapper.map(updatedActivityTypeDTO, existingActivityType);

					activityTypeRepository.save(existingActivityType);

					response.setMessage("Activity type updated successfully");
					response.setStatusCode(HttpStatus.OK.value());
					response.setPayload(existingActivityType);
				}
			} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
			}
		    return response;
	  }

	  @Override
	  public CustomResponse<?> deleteActivityTypeById(Long ActivityTypeId) throws NoResourceFoundException {
		  CustomResponse<String> response = new CustomResponse<>();

		  try {
			  if (activityTypeRepository.existsById(ActivityTypeId)) {
				  activityTypeRepository.deleteById(ActivityTypeId);

				  response.setMessage("Deleted");
				  response.setStatusCode(HttpStatus.OK.value());
				  response.setPayload("Activity type with id "+ActivityTypeId+" deleted successfully");
			  } else {
				  response.setMessage("Activity type with id "+ActivityTypeId+" not found");
				  response.setStatusCode(HttpStatus.NOT_FOUND.value());
				  response.setSuccess(false);
			  }
		  } catch (Exception e) {
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			  response.setSuccess(false);
		  }
		    return response;
	  }

	  
}
