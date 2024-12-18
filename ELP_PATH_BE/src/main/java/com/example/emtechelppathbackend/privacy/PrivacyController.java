package com.example.emtechelppathbackend.privacy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/privacy")
public class PrivacyController {

    @Autowired
    private PrivacyService privacyService;

    @GetMapping("/{userId}/view")
    public ResponseEntity<?> getPrivacyByUserId(@PathVariable Long userId) {
       var response = privacyService.getPrivacyByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updatePrivacy(

            @PathVariable Long userId,
            @RequestBody PrivacyEntity updatedPrivacy) {
        var response = privacyService.updatePrivacy1(userId, updatedPrivacy);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        @PostMapping("/add/{userId}")
    public ResponseEntity<?>addPrivacy(@PathVariable Long userId,
                                       @RequestBody PrivacyEntity addPrivacy){
        var response=privacyService.addPrivacy(userId, addPrivacy);
        return ResponseEntity.status(response.getStatusCode()).body(response);
        }

}

