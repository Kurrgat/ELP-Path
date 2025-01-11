package com.example.emtechelppathbackend.application.schoolhistory;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SchoolHistoryService {

	  List<SchoolHistory> displaySchoolHistoryByApplicationId(Long applicationId) throws NoResourceFoundException;
}
