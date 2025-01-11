package com.example.emtechelppathbackend.hubs.hubsv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/hubs")
public class HubControllerv2 {
    private final HubServicev2 hubService;
    private final ModelMapper modelMapper;


    @PostMapping("/create")
    public ResponseEntity<?> createHub(
            @ModelAttribute HubDTOv2 hubDto, @RequestParam(value = "file") MultipartFile file) {
        try {
            Hubv2 hub = modelMapper.map(hubDto, Hubv2.class);
            var response = hubService.createHub(hub, file);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @GetMapping("/{userId}/view")
    public ResponseEntity<?> getHubByIdAndUserId( @PathVariable Long userId) {
        var response = hubService.getHubByUserId( userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{hubId}/display-hub-members")
    public ResponseEntity<?> getHubMembers(@PathVariable Long hubId) {



            var members = hubService.getHubMembersByHubId(hubId);


            return ResponseEntity.status(members.getStatusCode()).body(members);

    }

    @GetMapping("/all")
    ResponseEntity<?> getAllHubs() {
        var response = hubService.getAllHubs();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{hubId}/update")
    public ResponseEntity<?> updateHub(@PathVariable(value = "hubId") Long hubId, @ModelAttribute HubDTOUpdatev2 hubDto,
                                           @RequestParam(value = "hubImage", required = false) MultipartFile hubImage) {
        try {
            var response = hubService.updateHubById(hubId, hubDto, hubImage);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error when processing image", e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }


    @PostMapping("/{userId}/{hubId}/join")
    public ResponseEntity<?> joinHub(@PathVariable(value = "userId") Long userId, @PathVariable(value = "hubId") Long hubId) {
        try {
            var response = hubService.joinHub(userId, hubId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/{hubId}/leave")
    public ResponseEntity<?> leaveHub(@PathVariable(value = "userId") Long userId, @PathVariable(value = "hubId") Long hubId) {
        try {

            var response = hubService.leaveHub(userId, hubId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{hubId}/delete")
    public ResponseEntity<?> deleteHub(@PathVariable(value = "hubId") Long hubId) {
        var response = hubService.deleteHub(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/v2/set-hub-admin/{hubId}/{userId}")
    ResponseEntity<?> setHubAdmin(@PathVariable Long hubId, @PathVariable Long userId) {
        var result = hubService.setHubAdmin(hubId, userId, false);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PutMapping("/v2/approve-joining-hub/{membershipId}")
    ResponseEntity<?> approveJoiningHub(@PathVariable Long membershipId, @RequestParam boolean approve) {
        var result = hubService.approveJoiningHub(membershipId, approve);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
    @GetMapping("/v2/get-membership-for-approval")
    ResponseEntity<?> getMembershipForApproval() {
        var result = hubService.getMembershipForApproval();
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @GetMapping("/get-hub/{hubId}")
    public ResponseEntity<?> getHub(@PathVariable  Long hubId) {
        var response = hubService.getHub(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
