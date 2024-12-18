package com.example.emtechelppathbackend.jobopportunities;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface JobOpportunityService {
    CustomResponse<?> addNewJobOpportunity(JobOpportunity jobOpportunity) throws NoResourceFoundException;

    CustomResponse<?> addNewJobOpportunityWithPoster(JobOpportunityDto jobOpportunityWithPoster) throws NoResourceFoundException, IOException;

    CustomResponse<JobOpportunityDtoView> viewJobOpportunityById(Long jobId) throws NoResourceFoundException;

    CustomResponse<List<JobOpportunityDtoView>> viewAllOpportunities();

    CustomResponse<Long> countAllOpportunities();

    CustomResponse<List<JobOpportunityDtoView>> viewActiveOpportunities();

    // CustomResponse<List<JobOpportunityDtoView>> viewOpportunitiesByOrganizationId(Long organizationId) throws NoResourceFoundException;

    CustomResponse<Long> countActiveOpportunities();

    CustomResponse<?> updateJobOpportunityById(Long jobId, JobOpportunityDto jobOpportunityUpdate) throws NoResourceFoundException;

    CustomResponse<?> updateJobOpportunityWithPosterById(Long jobId, JobOpportunityDto jobOpportunityWithPoster) throws NoResourceFoundException, IOException;

    CustomResponse<?> deleteJobOpportunityById(Long jobId) throws NoResourceFoundException;

    void notifyUsersAboutOpportunitiesWithPosters(JobOpportunity jobOpportunity);

    void notifyUsersAboutOpportunities(JobOpportunity jobOpportunity);

    String getImagesPath();

    CustomResponse<?> easyApply(Long userId,Long jobId);

    CustomResponse<?> countJobApplications(Long opportunityId);

    CustomResponse<?> searchOpportunity(String keyword);
}
