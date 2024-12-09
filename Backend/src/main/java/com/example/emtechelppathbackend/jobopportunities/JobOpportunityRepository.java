package com.example.emtechelppathbackend.jobopportunities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {

    List<JobOpportunity> findJobOpportunitiesByApplicationDeadLineIsBefore(LocalDateTime currentTime);

    Long countJobOpportunitiesByApplicationDeadLineIsBefore(LocalDateTime specifiedTime);

    // List<JobOpportunity> findJobOpportunitiesByOrganization(Organization organization);
}
