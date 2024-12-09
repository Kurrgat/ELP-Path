package com.example.emtechelppathbackend.school;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/schools")
public class SchoolController {

	  private final SchoolService schoolService;

	  @GetMapping("/all")
	  public ResponseEntity<CustomResponse<List<School>>> displayAllSchools() {
		   var schools = schoolService.displayAllSchools();
		    return  ResponseEntity.status(schools.getStatusCode()).body(schools);
	  }

	  @GetMapping("/display/{id}")
	  public ResponseEntity<CustomResponse<Optional<School>>> displaySchoolById(@PathVariable Long id) {
		   CustomResponse<Optional<School>> school = schoolService.displaySchoolById(id);
				return ResponseEntity.status(school.getStatusCode()).body(school);
	  }

	  @GetMapping("/{applicationId}/display-school")
	  public ResponseEntity<School> getSchoolByApplicationId(@PathVariable Long applicationId) {
		    School school = schoolService.getSchoolByApplicationId(applicationId);
		    if (school != null) {
				return new ResponseEntity<>(school, HttpStatus.OK);
		    } else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
	  }

	  @PostMapping("/add-new-school")
	  public ResponseEntity<School> addNewSchool(@RequestBody School school) {
		    School addedSchool = schoolService.addNewSchool(school);
		    return new ResponseEntity<>(addedSchool, HttpStatus.CREATED);
	  }

	  @PutMapping("/update/{id}")
	  public ResponseEntity<School> updateSchoolById(@PathVariable Long id, @RequestBody School updatedSchool) {
		    School updated = schoolService.updateSchoolById(id, updatedSchool);
		    if (updated != null) {
				return new ResponseEntity<>(updated, HttpStatus.OK);
		    }
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	  }

	  @DeleteMapping("/delete/{id}")
	  public ResponseEntity<Void> deleteSchoolById(@PathVariable Long id) {
		    boolean deleted = schoolService.deleteSchoolById(id);
		    if (deleted) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	  }

}
