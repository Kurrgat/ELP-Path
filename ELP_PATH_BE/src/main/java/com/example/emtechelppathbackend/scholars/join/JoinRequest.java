package com.example.emtechelppathbackend.scholars.join;

import com.example.emtechelppathbackend.scholars.VerifyScholarRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/join-request")
@Slf4j
public class JoinRequest {

	  private final JoinProcedure joinProcedure;

	  @PostMapping("/{pfNumberOrScholarCode}/join")
	  public ResponseEntity<?> authenticateAlumni(@PathVariable String pfNumberOrScholarCode) {
		    var response = joinProcedure.authenticateUserInput(pfNumberOrScholarCode);
		    return ResponseEntity.status(response.getStatusCode()).body(response);
	  }

	  @PostMapping("/alumni/verify-details")
	  public ResponseEntity<?> verifyAlumni(@RequestBody VerifyScholarRequest request) {
		  System.out.println("Received request: " +request);
		var response = joinProcedure.verifyScholar(request);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	  }

	  @PostMapping("/alumni/register")
	  public ResponseEntity<?> registerAlumni(@RequestBody RegisterAsUserRequest request) {
		var response = joinProcedure.register(request);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	  }
}
