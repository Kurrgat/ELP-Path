package com.example.emtechelppathbackend.Career;


import com.example.emtechelppathbackend.utils.CustomResponse;

public interface CareerService {
    CareerDto addSkills(CareerDto careerDto, Long userId);
    CustomResponse<?> viewCareerByUserId(Long userId);
    CustomResponse<?> updateCareer(Long userId,Long careerId,CareerDto careerDto);



   CustomResponse<?> deleteCareerById(Long id);
}
