package com.example.emtechelppathbackend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    @Autowired
    private final CommentService commentService;


    @PostMapping("/add/{userId}/{feedId}")
    public ResponseEntity<?> addComment(@PathVariable Long userId,@PathVariable Long feedId, @RequestBody CommentDto commentDto) {
        var response = commentService.addComment(userId,feedId, commentDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/total/{feedId}")
    public ResponseEntity<?> getTotalComments( @PathVariable Long feedId) {
        var response = commentService.getTotalComments(feedId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all/{feedId}")
    public ResponseEntity<?> getAllComments( @PathVariable Long feedId) {
        var response = commentService.getAllComments(feedId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
