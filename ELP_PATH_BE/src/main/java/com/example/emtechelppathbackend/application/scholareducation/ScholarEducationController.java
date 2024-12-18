package com.example.emtechelppathbackend.application.scholareducation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholar/education")
public class ScholarEducationController {
	  private final ScholarEducationService scholarEducationService;

	  public ScholarEducationController(ScholarEducationService scholarEducationService) {
		    this.scholarEducationService = scholarEducationService;
	  }

	  @PostMapping("/{scholarId}/add")
	  public ResponseEntity<ScholarEducationDto> addScholarEducation(@PathVariable(value = "scholarId") Long scholarId, @RequestBody ScholarEducationDto scholarEducationDto) {
		    return new ResponseEntity<>(scholarEducationService.addScholarEducation(scholarEducationDto, scholarId), HttpStatus.CREATED);
	  }

	  @GetMapping("/{scholarId}/view")
	  public List<ScholarEducationDto> viewScholarEducation(@PathVariable(value = "scholarId") Long scholarId) {
		    return scholarEducationService.findByApplicationId(scholarId);
	  }

	  @PutMapping("/update/{scholarEducationId}")
	  public ResponseEntity<ScholarEducationDto> updateScholarEducation(
		    @PathVariable Long scholarEducationId,
		    @RequestBody ScholarEducationDto updatedScholarEducationDto) {
		    ScholarEducationDto updatedScholarEducation = scholarEducationService.updateScholarEducation(scholarEducationId, updatedScholarEducationDto);
		    return ResponseEntity.ok(updatedScholarEducation);
	  }

	  @DeleteMapping("/delete/{scholarEducationId}")
	  public ResponseEntity<String> deleteScholarEducation(@PathVariable Long scholarEducationId) {
		    scholarEducationService.deleteScholarEducationById(scholarEducationId);
		    return ResponseEntity.ok("Scholar education deleted successfully");
	  }
}
