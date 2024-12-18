package com.example.emtechelppathbackend.activitytypes.activitytypesv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ActivityTypeServicev2 {
	  CustomResponse<ActivityTypeV2> addNewActivityType(ActivityTypeDTOv2 activityType);

	  CustomResponse<List<ActivityTypeV2>> displayAllActivityTypes() throws NoResourceFoundException;

	  CustomResponse<ActivityTypeV2> displayActivityTypeById(Long activityTypeId) throws NoResourceFoundException;

	  CustomResponse<ActivityTypeV2> updateActivityTypeById(ActivityTypeDTOv2 updatedActivityTypeDTO, Long activityTypeId) throws NoResourceFoundException;

	  CustomResponse<?> deleteActivityTypeById(Long activityTypeId) throws NoResourceFoundException;
}
