package com.example.emtechelppathbackend.jobopportunities;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/opportunities")
@Slf4j
public class JobOpportunityController {
    private final JobOpportunityService jobOpportunityService;

    @PostMapping("/job/create")
    public ResponseEntity<?> addJobOpportunity(@RequestBody JobOpportunity jobOpportunity) {
        try {
            var response = jobOpportunityService.addNewJobOpportunity(jobOpportunity);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error in retrieving organization", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error processing request", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/job/create-with-poster")
    // @RequestMapping(value =  "/{organizationId}/create-with-poster", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<?> addJobOpportunityWithPoster(@ModelAttribute JobOpportunityDto jobOpportunity) {
        log.info("null: "+jobOpportunity);
        try {
            var response = jobOpportunityService.addNewJobOpportunityWithPoster(jobOpportunity);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error in retrieving organization", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error processing image", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error processing request", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewAll() {
        try {
            var response = jobOpportunityService.viewAllOpportunities();
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{jobId}/view")
    public ResponseEntity<?> viewJobById(@PathVariable Long jobId) {
        try {
            var response = jobOpportunityService.viewJobOpportunityById(jobId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // @GetMapping("/{organizationId}/view-by-organization")
    // public ResponseEntity<?> viewJobByOrganizationId(@PathVariable Long organizationId) {
    //     try {
    //         var response = jobOpportunityService.viewOpportunitiesByOrganizationId(organizationId);
    //         return ResponseEntity.status(response.getStatusCode()).body(response);
    //     } catch (NoResourceFoundException e) {
    //         return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
    //     }
    // }

    @GetMapping("/count-all")
    public ResponseEntity<?> countOpportunities() {
        var response = jobOpportunityService.countAllOpportunities();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-active")
    public ResponseEntity<?> countActiveOpportunities() {
        var response = jobOpportunityService.countActiveOpportunities();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/view-active")
    public ResponseEntity<?> viewValidOpportunities() {
        try {
            var response = jobOpportunityService.viewActiveOpportunities();
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{jobId}/update")
    public ResponseEntity<?> updateJobById(@PathVariable Long jobId, @RequestBody JobOpportunityDto jobOpportunityDtoUpdate) {
        try {
            var response = jobOpportunityService.updateJobOpportunityById(jobId, jobOpportunityDtoUpdate);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{jobId}/update-with-poster")
    public ResponseEntity<?> updateJobWithPosterById(@PathVariable Long jobId, @ModelAttribute @Valid JobOpportunityDto jobOpportunityDtoUpdate) {
        try {
            var response = jobOpportunityService.updateJobOpportunityWithPosterById(jobId, jobOpportunityDtoUpdate);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error processing image", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error processing request", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{jobId}/delete")
    public ResponseEntity<?> deleteJobById(@PathVariable Long jobId) {
        try {
            var response = jobOpportunityService.deleteJobOpportunityById(jobId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("processed", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
