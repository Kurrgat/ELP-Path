package com.example.emtechelppathbackend.jobopportunities.jobapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface JobApplicationRepository  extends JpaRepository<JobApplication,Long> {

@Query(value = "SELECT COUNT(*) FROM job_application WHERE opportunity_id = :opportunityId",nativeQuery = true)
    Optional<Long> findByOpportunityId(Long opportunityId);
interface JobApplicationInterface{
    Integer getOpportunityId();
    String getJobTitle();
    String getJobType();
    String getOrganization();
   Integer getCount();

}
@Query(value = "SELECT \n" +
            "    ja.opportunity_id AS opportunityId,\n" +
            "    jo.job_title AS jobTitle,\n" +
            "    jo.job_type AS jobType,\n" +
            "    jo.organization AS organization,\n" +
            "    COUNT(ja.id) AS count\n" +
            "FROM \n" +
            "    job_application ja\n" +
            "JOIN \n" +
            "    job_opportunities jo ON ja.opportunity_id = jo.id\n" +
            "WHERE \n" +
            "    jo.record_time >= :startDate AND jo.record_time <= :endDate\n" +
            "GROUP BY \n" +
            "    ja.opportunity_id, jo.job_title, jo.job_type, jo.organization", nativeQuery = true)


    Collection<JobApplicationInterface> getJobOpportunityReport(LocalDate startDate, LocalDate endDate);
}
