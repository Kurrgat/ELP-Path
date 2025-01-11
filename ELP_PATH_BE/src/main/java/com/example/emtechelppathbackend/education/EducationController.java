package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education")
public class EducationController {
    private final EducationService educationService;
    private final ModelMapper modelMapper;

    public EducationController(EducationService educationService, ModelMapper modelMapper) {
        this.educationService = educationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/{userId}/{institutionId}/{courseId}/{countryId}/create")
    public ResponseEntity<?>addEducation( @PathVariable Long userId,  @PathVariable Long institutionId, @PathVariable Long courseId,@PathVariable Long countryId, @RequestBody AddEducationDTO addEducationDTO) {
     var response = educationService.addEducation(addEducationDTO,userId, courseId, institutionId, countryId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/{userId}/view")
    public ResponseEntity<CustomResponse<List<Education>>> getEducation(@PathVariable(value = "userId") Long userId){
        var education= educationService.getEducationByUserId(userId);
        return ResponseEntity.status(education.getStatusCode()).body(education);
    }

    @PutMapping("/{userId}/{id}/{courseId}/{institutionId}/{countryId}/update")
    public ResponseEntity<?> updateUserEducation(@PathVariable(value = "userId")Long userId, @PathVariable Long id,  @PathVariable Long institutionId,  @PathVariable Long courseId,@PathVariable Long countryId, @RequestBody AddEducationDTO addEducationDTO){
        var newEducation = educationService.updateEducation(userId, id, courseId, institutionId,countryId, addEducationDTO);
       return ResponseEntity.status(newEducation.getStatusCode()).body(newEducation);
    }
    @DeleteMapping("/{userId}/{educationId}/delete")
    public ResponseEntity<?>deleteEducation(@PathVariable(value = "userId")Long userId,@PathVariable(value = "educationId")Long educationId){
       var response= educationService.deleteEducation(userId, educationId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("get-universities/{countryName}")
    public  ResponseEntity<?> fetchInstitutions(@PathVariable String countryName){
        var response  = educationService.fetchInstitutions(countryName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("universities/{id}")
    public  ResponseEntity<?> findUniversityById(@PathVariable Long id){
        var response  = educationService.findUniversityById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/courses/all")
    public ResponseEntity<?> fetchCourses(){
        var response = educationService.fetchCourses();
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/course-clusters/all")
    public ResponseEntity<?> fetchCourseClusters(){
        var response = educationService.fetchCourseClusters();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/course-clusters/{clusterId}")
    public ResponseEntity<?> fetchCourseClusterById(@PathVariable Long clusterId){
        var response = educationService.findCourseClusterById(clusterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/clusters/count/")
    public ResponseEntity<?> getCourseClusters(){
        var response = educationService.getCourseClusters();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> fetchCourseById(@PathVariable Long courseId){
        var response = educationService.findCourseById(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-education-levels")
    public ResponseEntity<?> getEducationLevelCounts(){
        var response = educationService.getEducationLevelCounts();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/fetch-by-education-levels/{educationLevel}")
    public ResponseEntity<?> fetchByEducationLevel(@PathVariable String educationLevel){
        var response = educationService.fetchByEducationLevel(educationLevel);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
