package com.example.emtechelppathbackend.privacy;

import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.profile.ProfileDto;
import com.example.emtechelppathbackend.profile.ProfileService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class PrivacyServiceImp implements PrivacyService {
    @Autowired
    private final PrivacyRepo privacyRepo;

    private  final ModelMapper modelMapper;

    @Autowired
    private final ProfileService profileService;
    @Autowired
    private final UsersRepository usersRepository;




        @Override
    public CustomResponse<ProfileDto> getPrivacyByUserId(Long userId) {
        CustomResponse<ProfileDto> response = new CustomResponse<>();
        try {
            PrivacyEntity privacyEntity = privacyRepo.findByUserId(userId);

            if (privacyEntity != null) {
                // Retrieve actual values from the Profile
                Profile profile = profileService.getProfileByUserId(userId).getPayload().getProfile();

                ProfileDto profileDto = modelMapper.map(profile, ProfileDto.class);

                // Check if email is not private
                if (!privacyEntity.isEmailPrivate()) {

                    profileDto.setEmail(profile.getEmail());
                } else {

                    profileDto.setEmail(null);
                }

                // Check if phone number is not private
                if (!privacyEntity.isPhoneNumberPrivate()) {
                    // Set phone number only if it's not private
                    profileDto.setPhoneNo(profile.getPhoneNo());
                } else {
                    // If phone number is private, set it to null
                    profileDto.setPhoneNo(null);
                }

                response.setPayload(profileDto);
                response.setMessage("Privacy data fetched successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            } else {
                response.setMessage("Privacy data not found for the user");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            // Print the stack trace to identify the issue
            e.printStackTrace();

            response.setMessage("Failed to fetch privacy data");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<?> updatePrivacy1(Long userId, PrivacyEntity updatedPrivacy) {
        CustomResponse<PrivacyEntity> response = new CustomResponse<>();
        try {
            Optional<PrivacyEntity> optionalExistingPrivacy = privacyRepo.findById(userId);

            // Check if the privacy entity with the given ID exists
            if (optionalExistingPrivacy.isPresent()) {
                PrivacyEntity existingPrivacy = optionalExistingPrivacy.get();

                // Log the values received in the request
                System.out.println("isEmailPrivate from request: " + updatedPrivacy.isEmailPrivate());
                System.out.println("isPhoneNumberPrivate from request: " + updatedPrivacy.isPhoneNumberPrivate());

                // Update the existing privacy entity fields based on the provided values from the request
                existingPrivacy.setIsEmailPrivate(updatedPrivacy.isEmailPrivate());
                existingPrivacy.setIsPhoneNumberPrivate(updatedPrivacy.isPhoneNumberPrivate());

                // Save the updated user privacy data
                PrivacyEntity updatedPrivacyEntity = privacyRepo.save(existingPrivacy);

                response.setMessage("User privacy updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(updatedPrivacyEntity);
            } else {
                response.setMessage("Privacy entity with ID " + userId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Failed to update user privacy: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> addPrivacy(Long userId, PrivacyEntity addPrivacy) {
        CustomResponse<PrivacyEntity> response = new CustomResponse<>();
        try {
            // Step 1: Retrieve the user based on the provided userId
            Optional<Users> optionalUser = usersRepository.findById(userId);

            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();

                // Step 2: Check if the privacy data already exists for the user
                Optional<PrivacyEntity> existingPrivacy = privacyRepo.findById(userId);

                if (existingPrivacy.isPresent()) {
                    // Privacy data already exists for the user
                    response.setMessage("Privacy information already exists for the user");
                    response.setStatusCode(HttpStatus.CONFLICT.value()); // HTTP 409 Conflict
                    response.setSuccess(false);
                    response.setPayload(existingPrivacy.get());
                } else {
                    // Step 3: Create a new PrivacyEntity instance and set its properties
                    PrivacyEntity newPrivacy = new PrivacyEntity();
                    newPrivacy.setEmailPrivate(addPrivacy.isEmailPrivate());
                    newPrivacy.setPhoneNumberPrivate(addPrivacy.isPhoneNumberPrivate());

                    // Step 4: Associate the created PrivacyEntity with the retrieved user
                    newPrivacy.setUser(user);

                    // Step 5: Save the PrivacyEntity to the database
                    PrivacyEntity savedPrivacy = privacyRepo.save(newPrivacy);

                    response.setMessage("Privacy information added successfully");
                    response.setStatusCode(HttpStatus.CREATED.value());
                    response.setSuccess(true);
                    response.setPayload(savedPrivacy);
                }
            } else {
                response.setMessage("User with ID " + userId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate key violation
            response.setMessage("Privacy information already exists for the user");
            response.setStatusCode(HttpStatus.CONFLICT.value()); // HTTP 409 Conflict
            response.setSuccess(false);
            response.setPayload(null);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Failed to add privacy: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }




}


