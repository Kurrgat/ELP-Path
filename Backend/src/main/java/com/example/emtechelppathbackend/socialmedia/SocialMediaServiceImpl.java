package com.example.emtechelppathbackend.socialmedia;


import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SocialMediaServiceImpl implements SocialMediaService {
    private final UsersRepository userRepository;
    private final SocialMediaRepository socialMediaRepository;

    public SocialMediaServiceImpl(UsersRepository userRepository, SocialMediaRepository socialMediaRepository) {
        this.userRepository = userRepository;
        this.socialMediaRepository = socialMediaRepository;
    }

    @Override
    public SocialMediaDto addSocialMediaLink(SocialMediaDto socialMediaDto, Long userId) {
        SocialMedia socialMedia = mapToEntity(socialMediaDto);
        Users users = userRepository.findById(userId).orElseThrow(()->new UserDetailsNotFoundException("User not found"));
        if (socialMediaRepository.findByUserId(userId) == null) {
            socialMedia.setUser(users);
            socialMedia.setId(socialMediaDto.getId());
            socialMedia.setFacebook(socialMediaDto.getFacebook());
            socialMedia.setLinkedIn(socialMediaDto.getLinkedIn());
            socialMedia.setGithub(socialMediaDto.getGithub());
            socialMedia.setTwitter(socialMediaDto.getTwitter());
            socialMedia.setInstagram(socialMediaDto.getInstagram());
            SocialMedia newSocial = socialMediaRepository.save(socialMedia);
            return mapToDto(newSocial);
        }
        throw new UserDetailsNotFoundException("Your social media links are already set, you can update them instead");
    }

    @Override
    public CustomResponse<SocialMediaDto> viewSocialMediaByUserId(Long userId) {
        CustomResponse<SocialMediaDto>response=new CustomResponse<>();
        try {
            SocialMedia socialMedia = socialMediaRepository.findByUserId(userId);
            var result= mapToDto(socialMedia);
            if (result==null) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No socialMedia found ");
                response.setPayload(null);}

            else {

                response.setPayload(result);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }

    @Override
    public SocialMediaDto updateSocialMedia( Long userId, SocialMediaDto socialMediaDto) {
        SocialMedia socialMedia = mapToEntity(socialMediaDto);
        Users users = userRepository.findById(userId).orElseThrow(()->new UserDetailsNotFoundException("User not found"));
        //SocialMedia socialMedia = socialMediaRepository.findById(socialId).orElseThrow(()->new UserDetailsNotFoundException("this socials does not exist"));
//        if (!Objects.equals(socialMedia.getUser().getId(), users.getId())){
//           throw new UserDetailsNotFoundException("social media do not belong to this user");
//        }
        if (users.getId() != null) {
            socialMedia.setLinkedIn(socialMediaDto.getLinkedIn());
            socialMedia.setGithub(socialMediaDto.getGithub());
            socialMedia.setFacebook(socialMediaDto.getFacebook());
            socialMedia.setTwitter(socialMediaDto.getTwitter());
            socialMedia.setInstagram(socialMediaDto.getInstagram());
            SocialMedia updateSocials = socialMediaRepository.save(socialMedia);
            return mapToDto(updateSocials);
        }
        throw new UserDetailsNotFoundException("User not found");
    }

    @Override
    public CustomResponse<?> updateSocialMediaByUserId(Long userId, SocialMediaDto socialMediaDto) {
        CustomResponse<SocialMediaDto> response = new CustomResponse<>();

        try {

            SocialMedia socialMedia = socialMediaRepository.findByUserId(userId);
            if (socialMedia == null) {
                throw new UserDetailsNotFoundException("Social Media not found for the user");
            }

            socialMedia.setLinkedIn(socialMediaDto.getLinkedIn());
            socialMedia.setGithub(socialMediaDto.getGithub());
            socialMedia.setFacebook(socialMediaDto.getFacebook());
            socialMedia.setInstagram(socialMediaDto.getInstagram());
            socialMedia.setTwitter(socialMediaDto.getTwitter());

            SocialMediaDto updatedSocialMediaDto = mapToDto(socialMedia);

            socialMediaRepository.save(socialMedia);

            response.setMessage("Social media updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(updatedSocialMediaDto);
        } catch (UserDetailsNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage("An error occurred while updating social media");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }


    @Override
    public void deleteSocialMedia(Long socialId, Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(()->new UserDetailsNotFoundException("User not found"));
        SocialMedia socialMedia = socialMediaRepository.findById(socialId).orElseThrow(()->new UsernameNotFoundException("These socials do not exist"));
        if (!Objects.equals(socialMedia.getUser().getId(), users.getId())){
            throw new UserDetailsNotFoundException("The given social media does not belong to this user");
        }
        socialMediaRepository.delete(socialMedia);
    }
    private SocialMediaDto mapToDto(SocialMedia socialMedia){
        SocialMediaDto socialMediaDto = new SocialMediaDto();
        socialMediaDto.setId(socialMedia.getId());
        socialMediaDto.setFacebook(socialMedia.getFacebook());
        socialMediaDto.setTwitter(socialMedia.getTwitter());
        socialMediaDto.setLinkedIn(socialMedia.getLinkedIn());
        socialMediaDto.setInstagram(socialMedia.getInstagram());

        socialMediaDto.setGithub(socialMedia.getGithub());
        return socialMediaDto;
    }
    private SocialMedia mapToEntity(SocialMediaDto socialMediaDto){
        SocialMedia socialMedia = new SocialMedia();
        socialMedia.setId(socialMediaDto.getId());
        socialMedia.setFacebook(socialMediaDto.getFacebook());
        socialMedia.setTwitter(socialMediaDto.getTwitter());
        socialMedia.setLinkedIn(socialMediaDto.getLinkedIn());
        socialMedia.setGithub(socialMediaDto.getGithub());

        socialMedia.setInstagram(socialMediaDto.getInstagram());

        return socialMedia;
    }
}
