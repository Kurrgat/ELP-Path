package com.example.emtechelppathbackend.application.wtf_application;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface ApplicationService {
	  Application addNewApplication(Application application);

	  Optional<Application> updateApplicationById(Long id, Application application);

	  void deleteApplicationById(Long id);

	  List<Application> displayApplications();

	  Application displayApplicationDetailsById(Long id);

	  long getTotalApplications();

	  long getAwardedApplications();

	  long getAwaitingApplications();

	  long getNotAwardedApplications();
/*
	  void addSchool(Application application, School school) throws Exception;

	  void transferSchool(Long applicationId, Long schoolId) throws Exception;

	  void endSchool(Long applicationId) throws Exception;

 */

	  Map<Integer, Long> getTotalApplicationsByYear();

	  Map<Integer, Long> getTotalAwardedApplicationsByYear();

	  Set<ApplicationDto> getApplicationsByBranchId(Long branchId) throws NoResourceFoundException;
}
