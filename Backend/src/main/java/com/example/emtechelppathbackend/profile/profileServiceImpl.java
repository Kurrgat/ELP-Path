package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.ResourceNotFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.feed.feedv2.FeedsDTOv2;
import com.example.emtechelppathbackend.feed.feedv2.FeedsServicev2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarDTO;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import com.example.emtechelppathbackend.security.roles.RoleDto;
import com.example.emtechelppathbackend.security.user.*;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
@Slf4j
public class profileServiceImpl implements ProfileService {
    private final ProfileRepo profileRepo;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final JobOpportunityService jobOpportunityService;
    private final FeedsServicev2 feedsServicev2;
    private final ScholarRepo scholarRepo;
    HostNameCapture hostNameCapture = new HostNameCapture();

    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;

    @Autowired
    ServerPortService serverPortService;



    @Override
    public void createProfile(ProfileDto profileDto, Long userId) throws NoResourceFoundException {
        CustomResponse<Profile> customResponse = new CustomResponse<>();
        Users profileOwner = userRepository.findById(userId)
                .orElseThrow(() -> new NoResourceFoundException("User with that id not found"));

        if (profileRepo.findProfileByUserId(userId) != null) {
            throw new UserDetailsNotFoundException("Your profile already exists, you can update it instead");
        }

        try {
            Profile profile = modelMapper.map(profileDto, Profile.class);
            profile.setUser(profileOwner);

//            if (profileImageFile != null && !profileImageFile.isEmpty()) {
//                if(Objects.requireNonNull(profileImageFile.getOriginalFilename()).endsWith(".jpeg") || profileImageFile.getOriginalFilename().endsWith(".png") || profileImageFile.getOriginalFilename().endsWith(".jpg")) {
//                    String imageName = profileImageFile.getOriginalFilename();
//                    String uniqueImageName = UUID.randomUUID()+imageName.substring(imageName.lastIndexOf('.'));
//
//                    profileImageFile.transferTo(uploadPath.resolve(uniqueImageName));
//
//                    profile.setProfileImage(uniqueImageName);
//                } else {
//                    customResponse.setMessage("Upload .jpeg, .png or .jpg images");
//                    customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//                    customResponse.setSuccess(false);
//                }
//            }

            var response = profileRepo.save(profile);

            customResponse.setMessage("Profile added successfully");
            customResponse.setStatusCode(HttpStatus.OK.value());
            customResponse.setPayload(response);
        } catch (Exception e) {
            customResponse.setMessage(e.getMessage());
            customResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            customResponse.setSuccess(false);
        }

    }


    @Override
    public CustomResponse<ProfileResponse> getProfileByUserId(Long userId) throws NoResourceFoundException{
        Profile existingProfile = profileRepo.findProfileByUserId(userId);
        ProfileResponse profileResponse = new ProfileResponse();
        Users user = userRepository.findById(userId).orElseThrow(()-> new NoResourceFoundException("User with that id not found"));

        if (existingProfile == null){
            throw new NoResourceFoundException("The user has not updated his/her profile");
        }

        if(user.getScholar() != null) {
            ScholarDTO scholarDTO = modelMapper.map(user.getScholar(), ScholarDTO.class);
            profileResponse.setScholarDTO(scholarDTO);
        }

        if(existingProfile.getProfileImage() != null && !existingProfile.getProfileImage().isEmpty()) {
            String profileImage = jobOpportunityService.getImagesPath()+existingProfile.getProfileImage();
            existingProfile.setProfileImage(profileImage);
        }

        CustomResponse<ProfileResponse> response = new CustomResponse<>();

        profileResponse.setUsersDto(modelMapper.map(user, UsersDto.class));
        profileResponse.setProfile(existingProfile);

        response.setMessage(HttpStatus.OK.getReasonPhrase());
        response.setStatusCode(HttpStatus.OK.value());
        response.setPayload(profileResponse);



        return response;
    }

    @Override
    public CustomResponse<List<Profile>> displayAllProfiles() {
        CustomResponse<List<Profile>>response=new CustomResponse<>();
        try {
            var result= profileRepo.findAll();
            if (result.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No application found ");
                response.setPayload(null);
            }
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
    public CustomResponse<?> updateProfileById(Long profileId, ProfileDtoUpdate newProfileDto)
            throws NoResourceFoundException {
        CustomResponse<ProfileDto> response = new CustomResponse<>();

        Optional<Profile> existingProfileOption = profileRepo.findById(profileId);

        if(existingProfileOption.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Profile with id "+profileId+" does not exist");
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
        } else {
            Profile existingProfile = existingProfileOption.get();

            if (existingProfile.getUser() != null) {
                try {
                    System.out.println("data: "+existingProfile);
                    System.out.println("update data "+newProfileDto);
                    //crooked way of preventing the changing of the userId
                    existingProfile.setUser(existingProfile.getUser());
                    Branch branch = existingProfile.getUser().getScholar().getBranch();
                    mapFromUpdate(existingProfile, newProfileDto);
                        // Save the updated profile
                        profileRepo.save(existingProfile);

                        ProfileDto responseDTO = new ProfileDto();

                            System.out.println("role "+existingProfile.getUser().getRole());
                            responseDTO.setUsersDto(modelMapper.map(existingProfile.getUser(), UsersDto.class));


                    System.out.println("role "+existingProfile.getUser().getRole());

                    modelMapper.map(existingProfile, responseDTO);
                    responseDTO.setHomeBranch(branch);

                        if (existingProfile.getProfileImage() != null && !existingProfile.getProfileImage().isEmpty()) {
                            responseDTO.setProfileImage(jobOpportunityService.getImagesPath() + existingProfile.getProfileImage());
                            System.out.println("image is: "+responseDTO.getProfileImage());
                        } else {
                            responseDTO.setProfileImage("");
                        }

                        response.setMessage("Profile updated successfully");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setPayload(responseDTO);
                } catch (Exception e) {
                    response.setMessage(e.getMessage());
                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setSuccess(false);
                }
            } else {
                    throw new NoResourceFoundException("User details for profile id "+existingProfile.getId()+" not found");
            }
        }
        return response;
    }

    public  CustomResponse<?> updateProfileImage(Long profileId,MultipartFile file) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Profile> optional = profileRepo.findById(profileId);
            if(optional.isEmpty()) {
                response.setMessage("Profile data of id "+profileId+" not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Profile existingProfile = optional.get();

                if(file != null && !file.isEmpty()) {
                    if(Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jpeg") || file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png")) {
                        String imageName = file.getOriginalFilename();
                        String uniqueFileName = UUID.randomUUID()+imageName.substring(imageName.lastIndexOf('.'));
                        file.transferTo(uploadPath.resolve(uniqueFileName));
                        existingProfile.setProfileImage(uniqueFileName);

                        // Save the updated profile
                        profileRepo.save(existingProfile);

                        response.setMessage("Profile Picture updated successfully");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setPayload(jobOpportunityService.getImagesPath()+existingProfile.getProfileImage());
                    } else {
                        response.setMessage("Upload a .jpeg, .jpg or .png image file");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    }
                }
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getClosePeople(Long userId) {
        CustomResponse<List<MorePeopleDto>> response = new CustomResponse<>();

        try {
            Profile userProfile = profileRepo.findProfileByUserId(userId);
            Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
            if(user.getScholar() == null) {
                throw new ResourceNotFoundException("Scholar", "userid", userId);
            } else {
                Scholar scholar = user.getScholar();
                String schoolName;
                String branchName;


                if((scholar.getSchool() != null && !scholar.getSchool().getSchoolName().isEmpty()) && (scholar.getBranch() != null && !scholar.getBranch().getBranchName().isEmpty())) {
                    schoolName = scholar.getSchool().getSchoolName();
                    branchName = scholar.getBranch().getBranchName();

                    List<MorePeopleInterface> peopleIMayKnow = profileRepo.getPeopleIKnow(schoolName, branchName);
                    if(peopleIMayKnow.size() > 1) {
                        List<MorePeopleDto> morePeopleDto = new ArrayList<>();
                        for(MorePeopleInterface item: peopleIMayKnow) {
                            MorePeopleDto morePeopleDto1 = modelMapper.map(item, MorePeopleDto.class);
                            if(item.getProfileImage() != null && !item.getProfileImage().isEmpty()) {
                                morePeopleDto1.setProfileImage(jobOpportunityService.getImagesPath()+item.getProfileImage());
                            }
                            morePeopleDto.add(morePeopleDto1);
                        }
                        response.setPayload(morePeopleDto);
                    } else {
                        List<MorePeopleInterface> availablePeople = profileRepo.getAvailablePeople();
                        List<MorePeopleDto> morePeopleDto = new ArrayList<>();
                        for(MorePeopleInterface item: availablePeople) {
                            MorePeopleDto morePeopleDto1 = modelMapper.map(item, MorePeopleDto.class);
                            if(item.getProfileImage() != null && !item.getProfileImage().isEmpty()) {
                                morePeopleDto1.setProfileImage(jobOpportunityService.getImagesPath()+item.getProfileImage());
                            }
                            morePeopleDto.add(morePeopleDto1);
                        }
                        response.setPayload(morePeopleDto);
                    }
                }

                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getPeople() {
        CustomResponse<List<ProfileDto1>> response = new CustomResponse<>();
        List<ProfileDto1> profileDtos = new ArrayList<>();

        try {
            List<Profile> existingProfiles = profileRepo.findAll();

            if (existingProfiles.isEmpty()) {
                throw new NoResourceFoundException("No profiles found");
            }

            for (Profile profile : existingProfiles) {
                Users user = profile.getUser();
                if (user != null && user.getScholar() != null) {
                    ScholarDTO scholarDTO = modelMapper.map(user.getScholar(), ScholarDTO.class);
                    ProfileDto1 profileDto1 = mapProfileToProfileDto1(profile, user);
                    profileDto1.setHomeBranch(scholarDTO.getBranch());

                    if (profile.getProfileImage() != null && !profile.getProfileImage().isEmpty()) {
                        String profileImage = jobOpportunityService.getImagesPath() + profile.getProfileImage();
                        profileDto1.setProfileImage(profileImage);
                    }
                    profileDtos.add(profileDto1);
                }

            }

            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(profileDtos);

        } catch (Exception e) {
            log.error("Error retrieving profiles and users: {}", e.getMessage(), e);
            response.setMessage("Error retrieving profiles and users");
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }

        return response;
    }


    private ProfileDto1 mapProfileToProfileDto1(Profile existingProfile, Users user) {
        if (existingProfile == null) {
            return null;
        }
        ProfileDto1 profileDto = new ProfileDto1();
        profileDto.setEmail(existingProfile.getEmail());
        profileDto.setWebsite(existingProfile.getWebsite());
        profileDto.setTitle(existingProfile.getTitle());
        UserDtoView userDtoView = new UserDtoView();

        userDtoView.setId(user.getId());
        userDtoView.setFirstName(user.getFirstName());
        userDtoView.setLastName(user.getLastName());
        userDtoView.setUsername(user.getUsername());
        profileDto.setUsers(userDtoView);

        return profileDto;
    }


    @Override
    public CustomResponse<UserSearchDto> getUserSearchData(Long userId) {
        CustomResponse<UserSearchDto> response = new CustomResponse<>();

        if (getProfileByUserId(userId).getStatusCode() != 500) {
            ProfileResponse profileResponse = getProfileByUserId(userId).getPayload();

            if (feedsServicev2.getFeedByUserId(userId).getStatusCode() != 500) {
                List<FeedsDTOv2> feedsDTOv2 = feedsServicev2.getFeedByUserId(userId).getPayload();
                UserSearchDto userSearchDto = mapToUserData(profileResponse, feedsDTOv2);

                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(userSearchDto);
            } else {
                response.setSuccess(false);
                response.setMessage(feedsServicev2.getFeedByUserId(userId).getMessage());
                response.setStatusCode(feedsServicev2.getFeedByUserId(userId).getStatusCode());
            }
        } else {
            response.setSuccess(false);
            response.setMessage(getProfileByUserId(userId).getMessage());
            response.setStatusCode(getProfileByUserId(userId).getStatusCode());
        }
        return response;
    }

    @Override
    public void deleteProfile(Long userId) {
        Profile userProfile = profileRepo.findProfileByUserId(userId);

        if (userProfile == null) {
            throw new UserDetailsNotFoundException("Profile not found for the user");
        }

        if (!Objects.equals(userProfile.getUser().getId(), userId)) {
            throw new UserDetailsNotFoundException("This profile is not yours");
        }

        profileRepo.delete(userProfile);
    }


    public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            try {
                int port = serverPortService.getPort();

                if(port > 1023) {
                    imagesPath = hostNameCapture.getHost()+":"+port+"/images/";
                } else {
                    log.debug("Port is reserved for system use");
                }
                // imagesPath = hostNameCapture.getHost()+":5555/images/";
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagesPath;
    }

    private void mapFromUpdate(Profile existingProfile, ProfileDtoUpdate profileDtoUpdate) {
        try {
            Users user = existingProfile.getUser();

            if((profileDtoUpdate.getEmail() != null && !profileDtoUpdate.getEmail().isEmpty() && (existingProfile.getEmail() != null && !(existingProfile.getEmail().equals(profileDtoUpdate.getEmail()))))) {
                existingProfile.setEmail(profileDtoUpdate.getEmail());
            }


            if (profileDtoUpdate.getTitle() != null && !profileDtoUpdate.getTitle().isEmpty() &&
                    (existingProfile.getTitle() == null || !existingProfile.getTitle().equals(profileDtoUpdate.getTitle()))) {
                existingProfile.setTitle(profileDtoUpdate.getTitle());
            }


            if (profileDtoUpdate.getWebsite() != null && !profileDtoUpdate.getWebsite().isEmpty() &&
                    (existingProfile.getWebsite() == null || !existingProfile.getWebsite().equals(profileDtoUpdate.getWebsite()))) {
                existingProfile.setWebsite(profileDtoUpdate.getWebsite());
            }


            if (profileDtoUpdate.getPhoneNo() != null && !profileDtoUpdate.getPhoneNo().equals(existingProfile.getPhoneNo())) {
                existingProfile.setPhoneNo(profileDtoUpdate.getPhoneNo());
            }


            if (profileDtoUpdate.getCurrentCountryofResidence() != null && !profileDtoUpdate.getCurrentCountryofResidence().isEmpty() &&
                    (existingProfile.getCurrentCountryofResidence() == null || !existingProfile.getCurrentCountryofResidence().equals(profileDtoUpdate.getCurrentCountryofResidence()))) {
                existingProfile.setCurrentCountryofResidence(profileDtoUpdate.getCurrentCountryofResidence());
            }

            if (profileDtoUpdate.getCurrentCityofResidence() != null && !profileDtoUpdate.getCurrentCityofResidence().isEmpty() &&
                    (existingProfile.getCurrentCityofResidence() == null || !existingProfile.getCurrentCityofResidence().equals(profileDtoUpdate.getCurrentCityofResidence()))) {
                existingProfile.setCurrentCityofResidence(profileDtoUpdate.getCurrentCityofResidence());
            }

            if (profileDtoUpdate.getYearOfSelection() != null && !profileDtoUpdate.getYearOfSelection().equals(existingProfile.getYearOfSelection())) {
                existingProfile.setYearOfSelection(profileDtoUpdate.getYearOfSelection());
            }

            if (profileDtoUpdate.getJobStatus() != null && !profileDtoUpdate.getJobStatus().equals(existingProfile.getJobStatus())) {
                existingProfile.setJobStatus(profileDtoUpdate.getJobStatus());
            }

            if (profileDtoUpdate.getScholarCategory() != null &&
                    !profileDtoUpdate.getScholarCategory().equals(existingProfile.getScholarCategory())) {
                existingProfile.setScholarCategory(profileDtoUpdate.getScholarCategory());
            }

            if (profileDtoUpdate.getUser() != null && profileDtoUpdate.getUser().getUsername() != null && !profileDtoUpdate.getUser().getUsername().isEmpty() &&
                    !existingProfile.getUser().getUsername().equals(profileDtoUpdate.getUser().getUsername())) {
                existingProfile.getUser().setUsername(profileDtoUpdate.getUser().getUsername());
            }

            if(profileDtoUpdate.getUser() != null && profileDtoUpdate.getUser().getFirstName() != null && !profileDtoUpdate.getUser().getFirstName().isEmpty() && !existingProfile.getUser().getFirstName().equals(profileDtoUpdate.getUser().getFirstName())) {
                user.setFirstName(profileDtoUpdate.getUser().getFirstName());
            }

            existingProfile.setUser(user);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }

    }

    private UserSearchDto mapToUserData(ProfileResponse profileResponse, List<FeedsDTOv2> feedsDTOv2) {
        UserSearchDto userSearchDto = new UserSearchDto();
        Profile profile = profileResponse.getProfile();
        UsersDto usersDto = profileResponse.getUsersDto();

        userSearchDto.setUserId(usersDto.getId());
        userSearchDto.setFirstName(usersDto.getFirstName());
        userSearchDto.setLastName(usersDto.getLastName());

        userSearchDto.setProfileId(profile.getId());
        userSearchDto.setEmail(profile.getEmail());
        userSearchDto.setTitle(profile.getTitle());
        userSearchDto.setWebsite(profile.getWebsite());
        userSearchDto.setPhoneNo(profile.getPhoneNo());
        userSearchDto.setProfileImage(profile.getProfileImage());
        userSearchDto.setCurrentCountryofResidence(profile.getCurrentCountryofResidence());
        userSearchDto.setCurrentCityofResidence(profile.getCurrentCityofResidence());
        userSearchDto.setYearOfSelection(profile.getYearOfSelection());
        userSearchDto.setJobStatus(profile.getJobStatus());
        userSearchDto.setScholarCategory(profile.getScholarCategory());

        userSearchDto.setFeeds(feedsDTOv2);

        return  userSearchDto;
    }

}