package com.example.emtechelppathbackend.application.schoolhistory;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolHistoryServiceImpl implements SchoolHistoryService {

	  private final SchoolHistoryRepository schoolHistoryRepository;

	  @Override
	  public List<SchoolHistory> displaySchoolHistoryByApplicationId(Long applicationId) throws NoResourceFoundException {

		    List<SchoolHistory> schoolHistory = schoolHistoryRepository.findByApplicationId(applicationId);

		    if (schoolHistory.isEmpty()) {
				throw new NoResourceFoundException("No School History Found for application Id:" + applicationId);
		    }
		    return schoolHistory;
	  }
}
