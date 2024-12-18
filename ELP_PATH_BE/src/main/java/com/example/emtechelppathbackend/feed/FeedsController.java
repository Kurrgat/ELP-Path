package com.example.emtechelppathbackend.feed;


import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/feeds")
public class FeedsController {

    private final FeedsRepository feedsRepository;
    private final UsersRepository userRepository;
    private final FeedsService feedsService;
    private final ImageRepository imageRepository;

    public FeedsController(FeedsRepository feedsRepository, UsersRepository userRepository, FeedsService feedsService, ImageRepository imageRepository) {
        this.feedsRepository = feedsRepository;
        this.userRepository = userRepository;
        this.feedsService = feedsService;
        this.imageRepository = imageRepository;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getFeed(@PathVariable Long id) {

        var response= feedsService.getFeedById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<CustomResponse<List<FeedsDto>>> getUserFeeds(@PathVariable Long id) {
        var response= feedsService.getFeedByUserId(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<FeedsDto> updateFeedsById(@PathVariable Long id, @RequestBody FeedsDto feedsDto) {
        return new ResponseEntity<>(feedsService.updateFeedsById(id, feedsDto), HttpStatus.OK);

    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, Boolean>> deleteFeed(@PathVariable Long id) {
        Feeds feed = feedsRepository.findById(id).orElseThrow();
        feedsRepository.delete(feed);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomResponse<List<FeedsDto>>> getAllUsersFeeds() {

        var response= feedsService.getAllUsersFeeds();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<?> uploadFeeds(@RequestParam(value ="file", required = false) List<MultipartFile> files,
                                         @PathVariable("userId") Long userId,
                                         @RequestParam(value ="description") String description
    ) {
        return new ResponseEntity<>(feedsService.uploadFeeds(files,userId, description), HttpStatus.OK);
    }


}
