package com.example.emtechelppathbackend.survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {

     List<SurveyEntity> findByHubId(Long hubId);



}
