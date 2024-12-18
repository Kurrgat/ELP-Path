package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.utils.CustomResponse;

import java.util.List;

public interface EducationService {
    CustomResponse<?> addEducation(AddEducationDTO addEducationDTO,Long userId,Long courseId,  Long institutionId,Long countryId);
    CustomResponse<List<Education>> getEducationByUserId(Long userId);
    CustomResponse<?> updateEducation(Long userId, Long id, Long courseId, Long institutionId,Long countryId, AddEducationDTO addEducationDTO);

    CustomResponse<?> deleteEducation(Long userId,Long educationId);

    CustomResponse<?> fetchInstitutions(String countryName);

    CustomResponse<?> findUniversityById(Long id);
    CustomResponse<?> fetchCourses();
    CustomResponse<?> fetchCourseClusters();
    CustomResponse<?> findCourseClusterById(Long clusterID);
    CustomResponse<?> findCourseById(Long courseId);

    CustomResponse<?>getCourseClusters();

    CustomResponse<?> getEducationLevelCounts();

    CustomResponse<?> fetchByEducationLevel(String educationLevel);
}
