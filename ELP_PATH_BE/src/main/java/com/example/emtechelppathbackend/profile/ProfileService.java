package com.example.emtechelppathbackend.profile;


import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
   CustomResponse<?>createProfile(ProfileDto profileDto, Long userId, Long countyId) throws NoResourceFoundException, IOException;

    CustomResponse<ProfileResponse> getProfileByUserId(Long userId) throws NoResourceFoundException;

    CustomResponse< List<Profile>> displayAllProfiles();

    CustomResponse<?> updateProfileImage(Long profileId, MultipartFile profileImage);

    CustomResponse<?> updateProfileByIdAndCountyId(Long profileId,Long countyId, ProfileDtoUpdate newProfileDto) throws NoResourceFoundException, IOException;

    CustomResponse<?> deleteProfile(Long userId) throws NoResourceFoundException;




    CustomResponse<?> getUserSearchData(Long userId);

    CustomResponse<?> getClosePeople(Long userId);


    CustomResponse<?> getPeople();


}