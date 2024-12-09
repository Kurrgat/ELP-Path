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

    @PostMapping("/{userId}/{courseId}/{institutionId}/create")
    public ResponseEntity<?>createEducation(@Valid @PathVariable(value = "userId") Long userId, @Valid @PathVariable(value = "institutionId") Long institutionId, @Valid @PathVariable(value = "courseId") Long courseId, @RequestBody AddEducationDTO addEducationDTO) {
     var response = educationService.addEducation(addEducationDTO,userId, institutionId, courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/{userId}/view")
    public ResponseEntity<CustomResponse<List<Education>>> getEducation(@PathVariable(value = "userId") Long userId){
        var education= educationService.getEducationByUserId(userId);
        return ResponseEntity.status(education.getStatusCode()).body(education);
    }

    @PutMapping("/{userId}/{educationId}/{courseId}/{institutionId}/update")
    public ResponseEntity<EducationDto> updateUserEducation(@PathVariable(value = "userId")Long userId, @PathVariable(value = "educationId") Long educationId, @Valid @PathVariable(value = "institutionId")Long institutionId, @Valid @PathVariable(value = "courseId") Long courseId, AddEducationDTO addEducationDTO){
        Education education = modelMapper.map(addEducationDTO, Education.class);
        Education newEducation = educationService.updateEducation(userId, educationId, courseId, institutionId, addEducationDTO);
        EducationDto educationResponse = modelMapper.map(newEducation, EducationDto.class);
        return ResponseEntity.ok().body(educationResponse);
    }
    @DeleteMapping("/{userId}/{educationId}/delete")
    public ResponseEntity<?>deleteEducation(@PathVariable(value = "userId")Long userId,@PathVariable(value = "educationId")Long educationId){
        educationService.deleteEducation(userId, educationId);
        return new ResponseEntity<>("education deleted",HttpStatus.OK);
    }

    @GetMapping("universities/all")
    public  ResponseEntity<?> fetchInstitutions(){
        var response  = educationService.fetchInstitutions();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("universities/{id}")
    public  ResponseEntity<?> findUniversityById(@PathVariable Long id){
        System.out.println("institution id is "+ id);
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

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> fetchCourseById(@PathVariable Long courseId){
        var response = educationService.findCourseById(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
