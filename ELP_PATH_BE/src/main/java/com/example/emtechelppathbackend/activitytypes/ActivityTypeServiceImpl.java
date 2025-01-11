package com.example.emtechelppathbackend.activitytypes;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityTypeServiceImpl implements ActivityTypeService {
	  private final ActivityTypeRepository activityTypeRepository;
	  private final ModelMapper modelMapper;

	  @Override
	  public ActivityType addNewActivityType(ActivityType ActivityType) {
		    return activityTypeRepository.save(ActivityType);
	  }

	  @Override
	  public CustomResponse<List<ActivityType>> displayAllActivityTypes() throws NoResourceFoundException {
		  CustomResponse<List<ActivityType>>response=new CustomResponse<>();
		  try {
			  List<ActivityType> activities = activityTypeRepository.findAll();

			  if(activities.isEmpty()){
				  response.setStatusCode(HttpStatus.NOT_FOUND.value());
				  response.setMessage("No activity found");
				  response.setPayload(null);
			  }else {

				  response.setPayload(activities);
				  response.setMessage("Found");
				  response.setStatusCode(HttpStatus.OK.value());
			  }
		  }catch (Exception e){
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		  }
		  return response;
	  }


	  @Override
	  public CustomResponse<ActivityType> displayActivityTypeById(Long activityTypeId) throws NoResourceFoundException {
		  CustomResponse<ActivityType>response=new CustomResponse<>();
		  Optional<ActivityType> activityType = activityTypeRepository.findById(activityTypeId);
		  ActivityTypeDTO responseDTO = modelMapper.map(activityType, ActivityTypeDTO.class);
		  try {

			  var result= activityTypeRepository.findById(activityTypeId);
			  if(result.isEmpty()){
				  response.setStatusCode(HttpStatus.NOT_FOUND.value());
				  response.setMessage("No activity  found for the provided Id");
				  response.setPayload(null);
			  }else {
				  ActivityType activity=result.get();
				  response.setPayload(activity);
				  response.setMessage("Found");
				  response.setStatusCode(HttpStatus.OK.value());
			  }
		  }catch (Exception e){
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		  }
		  return response;
	  }



	  @Override
	  public ActivityType updateActivityTypeById(ActivityTypeDTO updatedActivityTypeDTO, Long activityTypeId) throws NoResourceFoundException {
		    Optional<ActivityType> existingActivityTypeOption = activityTypeRepository.findById(activityTypeId);
		    ActivityType existingActivityType = existingActivityTypeOption.orElseThrow(() -> new NoResourceFoundException("The ActivityType to be updated does not exist"));

		    modelMapper.map(updatedActivityTypeDTO, existingActivityType);

		    return activityTypeRepository.save(existingActivityType);
	  }

	  @Override
	  public void deleteActivityTypeById(Long ActivityTypeId) throws NoResourceFoundException {
		    ActivityType ActivityTypeToDelete = activityTypeRepository.findById(ActivityTypeId)
				.orElseThrow(() -> new NoResourceFoundException("ActivityType to be deleted not found"));
		    activityTypeRepository.delete(ActivityTypeToDelete);
	  }
}
