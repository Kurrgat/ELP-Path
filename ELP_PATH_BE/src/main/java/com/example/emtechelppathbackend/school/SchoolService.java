package com.example.emtechelppathbackend.school;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SchoolService {
   CustomResponse<List<School>> displayAllSchools();

    CustomResponse<Optional<School>> displaySchoolById(Long id);

    School addNewSchool(School school);

    School updateSchoolById(Long id, School updatedSchool);

    boolean deleteSchoolById(Long id);

    School getSchoolByApplicationId(Long applicationId);
}
