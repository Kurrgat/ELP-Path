package com.example.emtechelppathbackend.socialmedia;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/socials")
public class SocialMediaController {
    private final SocialMediaService socialMediaService;

    public SocialMediaController(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }
    @PostMapping("/{userId}/add")
    public ResponseEntity<SocialMediaDto>addSocialMedia(@PathVariable(value = "userId")Long userId, @RequestBody SocialMediaDto socialMediaDto){
        return new ResponseEntity<>(socialMediaService.addSocialMediaLink(socialMediaDto,userId), HttpStatus.CREATED);
    }
    @GetMapping("/{userId}/view")
    public ResponseEntity<CustomResponse<SocialMediaDto>> viewSocialMedia(@PathVariable(value = "userId")Long userId){
        var response= socialMediaService.viewSocialMediaByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateSocialMedia(@PathVariable(value = "userId")Long userId,@RequestBody SocialMediaDto socialMediaDto){
        var response = socialMediaService.updateSocialMediaByUserId(userId, socialMediaDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
