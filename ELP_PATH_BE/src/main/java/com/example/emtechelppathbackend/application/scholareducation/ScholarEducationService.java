package com.example.emtechelppathbackend.application.scholareducation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScholarEducationService {
    ScholarEducationDto addScholarEducation(ScholarEducationDto scholarEducationDto,Long applicationId);
    List<ScholarEducationDto>findByApplicationId(Long applicationId);

    ScholarEducationDto updateScholarEducation(Long educationId, ScholarEducationDto scholarEducationDto);

    void deleteScholarEducationById(Long scholarEducationId);
}
