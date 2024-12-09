package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.utils.CustomResponse;

import java.util.List;

public interface EducationService {
    CustomResponse<?> addEducation(AddEducationDTO addEducationDTO,Long userId,Long courseId,  Long institutionId);
    CustomResponse<List<Education>> getEducationByUserId(Long userId);
    Education updateEducation(Long userId, Long educationId, Long courseId, Long institutionId, AddEducationDTO addEducationDTO);
    Education updateUserEducation(Long userId, Long educationId, Long courseId, Long institutionId, AddEducationDTO addEducationDTO);
    void deleteEducation(Long userId,Long educationId);

    CustomResponse<?> fetchInstitutions();

    CustomResponse<?> findUniversityById(Long id);
    CustomResponse<?> fetchCourses();
    CustomResponse<?> fetchCourseClusters();
    CustomResponse<?> findCourseClusterById(Long clusterID);
    CustomResponse<?> findCourseById(Long courseId);
}
