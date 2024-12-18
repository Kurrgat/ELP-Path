package com.example.emtechelppathbackend.feed.feedv2;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/v2/feeds")
@Slf4j
public class FeedsControllerV2 {

    private final FeedsServicev2 feedsService;

    public FeedsControllerV2(FeedsServicev2 feedsService) {
        this.feedsService = feedsService;
    }


        @GetMapping("/{id}")
    public ResponseEntity<?> getFeedv2(@PathVariable Long id) {
        var response = feedsService.getFeedById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/images")
    public ResponseEntity<?> getFeedImages() throws IOException {
    // Load the image from the classpath (src/main/resources/images)

    try {
    ClassPathResource resource = new ClassPathResource("images/" + "638797d0-a7e7-4a19-915f-3aab3a667e5a.jpg");

        if (resource.exists() && resource.isReadable()) {
            InputStream inputStream = resource.getInputStream();
            byte[] imageData = IOUtils.toByteArray(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (IOException e) {
        return ResponseEntity.badRequest().build();
    }
}

    @GetMapping("/{userId}/view")
    public ResponseEntity<?> getUserFeedsv2(@PathVariable Long userId) {
        var response = feedsService.getFeedByUserId(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{userId}/{id}/edit")
    public ResponseEntity<?> updateFeedsByIdv2(@PathVariable  Long userId,@PathVariable Long id, @RequestBody FeedsDTOv2 feedsDto) {
        var response = feedsService.updateFeedsById(userId, id, feedsDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}/{userId}/delete")
    public ResponseEntity<?> deleteFeed(@PathVariable Long id, @PathVariable Long userId) {
        var response = feedsService.deleteFeed(id,userId);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsersFeedsv2() {
        var response = feedsService.getAllUsersFeeds();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> uploadFeedsv2(
            @PathVariable("userId") Long userId, @ModelAttribute FeedsDTO feedsDTO
    )  {
        var response = feedsService.uploadFeeds(userId, feedsDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/feeds-hub/{hubId}")
    public ResponseEntity<?> getAllFeedsByHubId(@PathVariable Long hubId) {
        var response = feedsService.getAllFeedsByHubId(hubId);
            return ResponseEntity.status(response.getStatusCode()).body(response);

    }
    @GetMapping("/feeds-chapter/{chapterId}")
    public ResponseEntity<?> getAllFeedsByChapterId(@PathVariable  Long chapterId) {
        var response = feedsService.getAllFeedsByChapterId(chapterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/get-feed/{feedId}")
    public ResponseEntity<?> getFeedById(@PathVariable Long feedId) {

        var response= feedsService.getFeedDetailsById(feedId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/all-user-feeds")
    public ResponseEntity<?> getAllFeedsv2() {
        var response = feedsService.getAllFeeds();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}