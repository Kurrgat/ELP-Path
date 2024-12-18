package com.example.emtechelppathbackend.socialmedia;
import com.example.emtechelppathbackend.socialmedia.SocialMediaDto;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SocialMediaService {
    SocialMediaDto addSocialMediaLink(SocialMediaDto socialMediaDto,Long userId);
    CustomResponse<SocialMediaDto> viewSocialMediaByUserId(Long userId);

    SocialMediaDto updateSocialMedia(Long userId,SocialMediaDto socialMediaDto);
    CustomResponse<?> updateSocialMediaByUserId(Long userId,SocialMediaDto socialMediaDto);
    void deleteSocialMedia(Long socialId,Long userId);
}
