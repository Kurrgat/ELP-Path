package com.example.emtechelppathbackend.application.schoolhistory;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/school-history")
public class SchoolHistoryController {

	  private final SchoolHistoryService schoolHistoryService;
	  @GetMapping("/{applicationId}/display")
	  public ResponseEntity<List<SchoolHistory>> displayHistoryByApplicationId(@PathVariable Long applicationId){
		    try {
				List<SchoolHistory> schoolHistories = schoolHistoryService.displaySchoolHistoryByApplicationId(applicationId);
				return new ResponseEntity<>(schoolHistories, HttpStatus.OK);
		    }catch (NoResourceFoundException e){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }catch (Exception e){
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	  }
}
