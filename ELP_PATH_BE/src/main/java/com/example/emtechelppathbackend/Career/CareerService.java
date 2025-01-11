package com.example.emtechelppathbackend.Career;


import com.example.emtechelppathbackend.utils.CustomResponse;

public interface CareerService {
    CustomResponse<?> addCareer(CareerDto careerDto, Long userId);
    CustomResponse<?> viewCareerByUserId(Long userId);
    CustomResponse<?> updateCareer(Long userId,Long careerId,CareerDto careerDto);



   CustomResponse<?> deleteCareer(Long id, Long userId);

    CustomResponse<?> updateToDateCareer(Long userId, Long careerId, UpdateCareerDto careerDto);
}
