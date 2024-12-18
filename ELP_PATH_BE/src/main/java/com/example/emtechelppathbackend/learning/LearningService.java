package com.example.emtechelppathbackend.learning;

import com.example.emtechelppathbackend.learning.enrol.EnrolDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface LearningService {
    CustomResponse<?> addCourse(LearningDto learningDto);

    CustomResponse<?> getLearningCourse();

    CustomResponse<?> editLearningCourse(LearningDto learningDto,Long learningId);

    CustomResponse<?> deleteLearningCourse(Long learningId);

    CustomResponse<?> enrolCourse(EnrolDto enrolDto, Long learningId, Long userId);
}
