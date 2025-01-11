package com.example.emtechelppathbackend.like;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private final LikeService likeService;

    @PostMapping("/add/{userId}/{feedId}")
    public ResponseEntity<?> addLike(@PathVariable Long userId,@PathVariable Long feedId, @RequestBody LikeEntity like) {
        var response = likeService.saveLike(userId,feedId, like);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unlikePost(@PathVariable Long id) {
      var response=  likeService.deleteLike(id);
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/total/{feedId}")
    public ResponseEntity<?> getTotalLikes(@PathVariable Long feedId) {
        var response = likeService.getTotalLikes(feedId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all/{feedId}")
    public ResponseEntity<?> getAllLikes(@PathVariable Long feedId) {
        var response = likeService.getAllLikes(feedId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
