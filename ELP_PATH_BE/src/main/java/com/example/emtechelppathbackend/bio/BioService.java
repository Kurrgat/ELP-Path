package com.example.emtechelppathbackend.bio;


import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Component;

@Component
public interface BioService{
    BioDto addBio(Long userId,BioDto bioDto);
    CustomResponse<?> updateBioByUserId(Long userId,Long bioId,BioDto bioDto);
    CustomResponse<BioDto> findBioByUserId(Long userId);
    CustomResponse<?> deleteBio(Long userId,Long bioId);
}
