package com.example.emtechelppathbackend.institutionchecker;

import com.example.emtechelppathbackend.like.LikeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/institution-checker")
@RequiredArgsConstructor
public class InstitutionCheckerController {

   private final InstitutionCheckerService institutionCheckerService;
    @PostMapping("/add")
    public ResponseEntity<?> addInstitutionChecker( @RequestBody InstitutionChecker institutionChecker) {
        var response = institutionCheckerService. addInstitutionChecker(institutionChecker);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
