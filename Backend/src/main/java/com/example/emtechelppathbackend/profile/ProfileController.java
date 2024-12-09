package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
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

    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDto profileDto, @PathVariable(value = "userId") Long userId) {
        try {
            System.out.println(profileDto +""+ userId);
            profileService.createProfile(profileDto, userId);

            return ResponseEntity.ok(new ResponseRecordOFMessages("Profile created successfully", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("Error while processing Image", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PutMapping("/{profileId}/update")
    public ResponseEntity<?> updateProfile(@PathVariable Long profileId,
                                           @RequestBody ProfileDtoUpdate updateProfileDto) {
        try {

            var response = profileService.updateProfileById(profileId, updateProfileDto);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), "something is missing"), HttpStatus.NOT_FOUND);
        } catch (UserDetailsNotFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), "User details not found"), HttpStatus.UNAUTHORIZED);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), "Error when processing Image"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        profileService.deleteProfile(userId);
        return new ResponseEntity<>(new ResponseRecordOFMessages("profile deleted successfully", null),HttpStatus.OK);
    }

    @GetMapping("/discover-people")
    public ResponseEntity<?> getPeople() {
        var response = profileService.getPeople();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
