package com.example.emtechelppathbackend.jobopportunities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {

    List<JobOpportunity> findJobOpportunitiesByApplicationDeadLineIsBefore(LocalDateTime currentTime);

    Long countJobOpportunitiesByApplicationDeadLineIsBefore(LocalDateTime specifiedTime);


    @Query(value = "SELECT * \n" +
            "FROM job_opportunities j\n" +
            "WHERE j.job_description LIKE CONCAT('%', :keyword, '%') \n" +
            "OR j.job_title LIKE CONCAT('%', :keyword, '%')\n" +
            "OR j.organization LIKE CONCAT('%', :keyword, '%')\n" +
            "OR j.job_type LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<JobOpportunity> findByKeyword(String keyword);


}


