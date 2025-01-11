package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import com.example.emtechelppathbackend.scholarcv.ScholarCvService;
import com.twilio.http.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final ScholarCvService scholarCvService;

    @PostMapping("/{userId}/{countyId}/create")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDto profileDto, @PathVariable(value = "userId") Long userId,@PathVariable Long countyId) throws IOException {
          var response=  profileService.createProfile(profileDto, userId,countyId);
          return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/all")
    public ResponseEntity<?> displayAllProfiles(){
        var response=profileService.displayAllProfiles();
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/{userId}/view")
    public ResponseEntity<?> getProfileByUserId(@PathVariable Long userId){
        var response=profileService.getProfileByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/{profileId}/{countyId}/update")
    public ResponseEntity<?> updateProfile(@PathVariable Long profileId,@PathVariable Long countyId,
                                           @RequestBody ProfileDtoUpdate updateProfileDto) throws IOException {

            var response = profileService. updateProfileByIdAndCountyId(profileId,countyId, updateProfileDto);
            return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @PutMapping(value = "/{profileId}/update-image")
    public ResponseEntity<?> updateProfileImage(@PathVariable Long profileId,@RequestParam MultipartFile file) {
        System.out.println(file);
        var response = profileService.updateProfileImage(profileId, file);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(value = "/get-user-data/{userId}")
    public  ResponseEntity<?> getUserSearchData(@PathVariable Long userId) {
        var response = profileService.getUserSearchData(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(value = "/people-i-know/{userId}")
    public ResponseEntity<?> getMorePeople(@PathVariable Long userId) {
        var response = profileService.getClosePeople(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteProfile(@PathVariable(value = "userId")Long userId){
        var response=profileService.deleteProfile(userId);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/discover-people")
    public ResponseEntity<?> getPeople() {
        var response = profileService.getPeople();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/cv/{userId}")
    public ResponseEntity<?> getCv(@PathVariable Long userId) {
        var response = scholarCvService.getCv(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
