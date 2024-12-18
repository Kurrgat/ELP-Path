package com.example.emtechelppathbackend.hubs;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hubs")
public class HubController {
    private final HubService hubService;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;


    @PostMapping("/create")
    public ResponseEntity<?> createHub(
            @ModelAttribute HubDTO hubDto, @RequestParam(value = "file") MultipartFile file) {
        try {
            Hub hub = modelMapper.map(hubDto, Hub.class);
            hubService.createHub(hub, file);
            return ResponseEntity.ok(new ResponseRecordOFMessages("Hub created successfully", null));
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @GetMapping("/{hubId}/view")
    public ResponseEntity<CustomResponse<HubDTO>> getHubById(@PathVariable(value = "hubId") Long hubId) {
        var response=hubService.getHubById(hubId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{hubId}/display-hub-members")
    public ResponseEntity<?> getHubMembers(@PathVariable Long hubId) {
           CustomResponse<Set<UsersDto>>members = hubService.getMembersByHubId(hubId);
            return  ResponseEntity.status(members.getStatusCode()).body(members);
    }

    @GetMapping("/all")
    ResponseEntity<CustomResponse<List<Hub>>> getAllHubs() {
        var response=hubService.getAllHubs();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{hubId}/update")
    public ResponseEntity<?> updateHub(@PathVariable(value = "hubId") Long hubId, @ModelAttribute HubDTOUpdate hubDto,
                                           @RequestParam(value = "hubImage", required = false) MultipartFile hubImage) {
        try {
            Hub updatedHub = hubService.updateHubById(hubId, hubDto, hubImage);
            HubDTO response = modelMapper.map(updatedHub, HubDTO.class);
            return ResponseEntity.ok(new ResponseRecordOFMessages("update successful", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error when processing image", e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }


    @PostMapping("/{userId}/{hubId}/join")
    public ResponseEntity<?> joinHub(@PathVariable(value = "userId") Long userId, @PathVariable(value = "hubId") Long hubId) {
        try {
            Hub joinedHub = hubService.joinHub(userId, hubId);
            return new ResponseEntity<>(joinedHub, HttpStatus.CREATED);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/{hubId}/leave")
    public ResponseEntity<?> leaveHub(@PathVariable(value = "userId") Long userId, @PathVariable(value = "hubId") Long hubId) {
        try {

            Hub leftHub = hubService.leaveHub(userId, hubId);
            return new ResponseEntity<>(leftHub, HttpStatus.OK);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{hubId}/delete")
    public ResponseEntity<?> deleteHub(@PathVariable(value = "hubId") Long hubId) {
        hubService.deleteHub(hubId);
        return new ResponseEntity<>("Hub deleted", HttpStatus.OK);
    }
}
