package com.example.emtechelppathbackend.application.wtf_application;

import com.example.emtechelppathbackend.school.SchoolService;
import com.example.emtechelppathbackend.customizedimports.ApiResponse;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")

public class ApplicationsController {

    private final ApplicationService applicationService;

    private final SchoolService schoolService;

    private final ModelMapper modelMapper;


    @GetMapping("/display-applications")
    public List<ApplicationDto> displayApplications() {
        return applicationService.displayApplications()
                .stream().map(application -> modelMapper
                        .map(application, ApplicationDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/display-applications/{id}")
    public ResponseEntity<ApplicationDto> displayApplicationDetails(@PathVariable(name = "id") Long id) {
        Application application = applicationService.displayApplicationDetailsById(id);

        //convert to DTO
        ApplicationDto applicationResponse = modelMapper.map(application, ApplicationDto.class);

        return ResponseEntity.ok().body(applicationResponse);
    }

    @GetMapping("/count-all-applications")
    public long countTotalApplicants(){
        return applicationService.getTotalApplications();
    }

    @GetMapping("/count-awaiting-applications")
    public long countTotalAwaitingApplicants(){
        return applicationService.getAwaitingApplications();
    }
    @GetMapping("/count-not-awarded-applications")
    public long countTotalNotAwardedApplicants(){
        return applicationService.getNotAwardedApplications();
    }
    @GetMapping("/count-awarded-applications")
    public long countTotalAwardedApplicants(){
        return applicationService.getAwardedApplications();
    }

    @GetMapping("/count-applications-by-year")
    public Map<Integer, Long> getTotalApplicationsByYear(){
        return applicationService.getTotalApplicationsByYear();
    }

    @GetMapping("/count-awarded-applications-by-year")
    public Map<Integer, Long> getTotalAwardedApplicationsByYear(){
        return applicationService.getTotalAwardedApplicationsByYear();
    }

    @PostMapping("/add-new-application")
    public ResponseEntity<ApplicationDto> addNewApplication(@RequestBody ApplicationDto applicationDto) {

        //convert Dto to entity
        Application applicationRequest = modelMapper.map(applicationDto, Application.class);


        Application application = applicationService.addNewApplication(applicationRequest);

        //convert entity to DTO
        ApplicationDto applicationResponse = modelMapper.map(application, ApplicationDto.class);

        return new ResponseEntity<ApplicationDto>(applicationResponse, HttpStatus.CREATED);
    }


    @GetMapping("{branchId}/display-applications")
    public ResponseEntity<?> diplayApplicationsByBranchId(@PathVariable Long branchId) {
        try {
            Set<ApplicationDto> applications = applicationService.getApplicationsByBranchId(branchId);
            return ResponseEntity.ok(applications);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/*
    @PostMapping("/{applicationId}/add-school/{schoolId}")
    public ResponseEntity<String> addSchoolToApplication
            (@PathVariable("applicationId") Long applicationId, @PathVariable("schoolId") Long schoolId) {
        Application application = applicationService.displayApplicationDetailsById(applicationId);
        Optional<School> possibleSchools = schoolService.displaySchoolById(schoolId);

        if (application == null || possibleSchools.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application or school not found.");
        }

        try {
            // Add the school to the application's history
            applicationService.addSchool(application, possibleSchools.get());

            return ResponseEntity.ok("School added to the application successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add school to the application: " + e.getMessage());
        }
    }

    @PostMapping("/transfer-school")
    public ResponseEntity<String> transferSchool(@RequestParam Long applicationId, @RequestParam Long schoolId){
        try {
            applicationService.transferSchool(applicationId, schoolId);
            return new ResponseEntity<>("transfer recorded successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/end-school")
    public ResponseEntity<String> endSchool(@RequestParam Long applicantId){
        try {
            applicationService.endSchool(applicantId);
            return new ResponseEntity<>("student school record successfully updated", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/
    @PutMapping("/update-application/{id}")
    public ResponseEntity<ApplicationDto> updateApplication(@PathVariable long id,
            @RequestBody ApplicationDto applicationDto) {

        //convert DTO to entity
        Application applicationRequest = modelMapper.map(applicationDto, Application.class);

        Optional<Application> application = applicationService.updateApplicationById(id, applicationRequest);

        //entity to DTO
        ApplicationDto applicationResponse = modelMapper.map(application, ApplicationDto.class);
       return ResponseEntity.ok().body(applicationResponse);
    }


    @DeleteMapping("/delete-application/{id}")
    public ResponseEntity<ApiResponse> deleteApplication(@PathVariable(name = "id") Long id) {
        applicationService.deleteApplicationById(id);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Application deleted successfully", HttpStatus.OK);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}