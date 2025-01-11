package com.example.emtechelppathbackend.activitytypes;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ActivityTypeService {
	  ActivityType addNewActivityType(ActivityType activityType);

	 CustomResponse< List<ActivityType>> displayAllActivityTypes() throws NoResourceFoundException;

	  CustomResponse<ActivityType> displayActivityTypeById(Long activityTypeId) throws NoResourceFoundException;

	  ActivityType updateActivityTypeById(ActivityTypeDTO updatedActivityTypeDTO, Long activityTypeId) throws NoResourceFoundException;

	  void deleteActivityTypeById(Long activityTypeId) throws NoResourceFoundException;
}
