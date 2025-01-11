package com.example.emtechelppathbackend.privacy;


import com.example.emtechelppathbackend.utils.CustomResponse;

public interface PrivacyService {


    CustomResponse<?> getPrivacyByUserId(Long userId);




    CustomResponse<?> updatePrivacy1(Long userId, PrivacyEntity updatedPrivacy);

    CustomResponse<?>addPrivacy(Long userId, PrivacyEntity addPrivacy);
}
