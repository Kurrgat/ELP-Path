package com.example.emtechelppathbackend.school;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.application.wtf_application.ApplicationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

	  private final SchoolRepository schoolRepository;
	  private final ApplicationRepository applicationRepository;

	  public CustomResponse<List<School>> displayAllSchools() {
		  CustomResponse<List<School>>response=new CustomResponse<>();
		  try {
		   var result= schoolRepository.findAll();
		  if(result.isEmpty()){
			  response.setStatusCode(HttpStatus.NOT_FOUND.value());
			  response.setMessage("No school found");
			  response.setPayload(null);
		  }else {

			  response.setPayload(result);
			  response.setMessage("Found");
			  response.setStatusCode(HttpStatus.OK.value());
		  }
	  }catch (Exception e){
		response.setMessage(e.getMessage());
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

	}
		  return response;
}


	  public CustomResponse<Optional<School>> displaySchoolById(Long id) {
		  CustomResponse<Optional<School>>response=new CustomResponse<>();
		  try {
			  var result= schoolRepository.findById(id);
			  if(result.isEmpty()){
				  response.setStatusCode(HttpStatus.NOT_FOUND.value());
				  response.setMessage("No activity found");
				  response.setPayload(null);
			  }else {

				  response.setPayload(result);
				  response.setMessage("Found");
				  response.setStatusCode(HttpStatus.OK.value());
			  }
		  }catch (Exception e){
			  response.setMessage(e.getMessage());
			  response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		  }
		  return response;
	  }


	  public School addNewSchool(School school) {
		    return schoolRepository.save(school);
	  }


	  public School updateSchoolById(Long id, School updatedSchool) {
		    Optional<School> optionalSchool = schoolRepository.findById(id);
		    if (optionalSchool.isPresent()) {
				School school = optionalSchool.get();
				school.setSchoolName(updatedSchool.getSchoolName());
				school.setAdditionalInformation(updatedSchool.getAdditionalInformation());
				return schoolRepository.save(school);
		    }
		    return null; // todo or throw an exception indicating the school doesn't exist
	  }

	  public boolean deleteSchoolById(Long id) {
		    if (schoolRepository.existsById(id)) {
				schoolRepository.deleteById(id);
				return true;
		    }
		    return false;
	  }

	  @Override
	  public School getSchoolByApplicationId(Long applicationId) {
		    Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
		    if (optionalApplication.isPresent()) {
				Application application = optionalApplication.get();
				return application.getSchool();
		    }
		    return null;
	  }

}
