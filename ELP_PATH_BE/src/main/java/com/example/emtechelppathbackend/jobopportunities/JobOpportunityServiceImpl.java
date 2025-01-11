package com.example.emtechelppathbackend.jobopportunities;

import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import com.example.emtechelppathbackend.jobopportunities.jobapplication.JobApplication;
import com.example.emtechelppathbackend.jobopportunities.jobapplication.JobApplicationRepository;
import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.profile.ProfileRepo;
import com.example.emtechelppathbackend.profile.ProfileService;
import com.example.emtechelppathbackend.security.user.UserService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobOpportunityServiceImpl implements JobOpportunityService {
    private final JobOpportunityRepository jobOpportunityRepository;
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final JobApplicationRepository jobApplicationRepository;

    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    HostNameCapture hostNameCapture = new HostNameCapture();
    String imagesPath;

    @Autowired
    ServerPortService serverPortService;


    @Override
    public CustomResponse<?> addNewJobOpportunity(JobOpportunity jobOpportunity) throws NoResourceFoundException {
        CustomResponse<JobOpportunity> response = new CustomResponse<>();

        try {
                jobOpportunityRepository.save(jobOpportunity);

                Long id = jobOpportunity.getId();
                log.info(""+id);
                response.setMessage("job opportunity created");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(jobOpportunity);
        } catch (Exception e) {
                log.info(e.getMessage());
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        
        CompletableFuture.runAsync(() -> notifyUsersAboutOpportunities(jobOpportunity));
        return response;
    }

    @Override
    public CustomResponse<?> addNewJobOpportunityWithPoster(JobOpportunityDto jobOpportunityWithPoster) throws NoResourceFoundException, IOException {
        CustomResponse<JobOpportunity> response = new CustomResponse<>();

        try {
            JobOpportunity newJobOpportunity = modelMapper.map(jobOpportunityWithPoster, JobOpportunity.class);

                if (jobOpportunityWithPoster.getJobPoster() != null && !jobOpportunityWithPoster.getJobPoster().isEmpty()) {
                    String imageName = jobOpportunityWithPoster.getJobPoster().getOriginalFilename();
                    assert imageName != null;
                    String extension = imageName.substring(imageName.lastIndexOf('.'));

                    if(imageName.endsWith(".jpeg") || imageName.endsWith(".png") || imageName.endsWith(".jpeg")) {
                        String uniqueName = UUID.randomUUID()+extension;

                        jobOpportunityWithPoster.getJobPoster().transferTo(uploadPath.resolve(uniqueName));
                        newJobOpportunity.setJobPoster(uniqueName);

                        jobOpportunityRepository.save(newJobOpportunity);

                        response.setMessage("Job opportunity created");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setSuccess(true);
                        response.setPayload(newJobOpportunity);
                    CompletableFuture.runAsync(()-> notifyUsersAboutOpportunitiesWithPosters(newJobOpportunity));
                    } else {
                        response.setMessage("Upload a .jpg, .png or .jpeg file");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    }
                }
                
                
        } catch (Exception e) {
            log.info(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;   
    }

    @Override
    public CustomResponse<JobOpportunityDtoView> viewJobOpportunityById(Long jobId) throws NoResourceFoundException {
        CustomResponse<JobOpportunityDtoView> response = new CustomResponse<>();

        try {
            Optional<JobOpportunity> jobOpportunity = jobOpportunityRepository.findById(jobId);

            if(jobOpportunity.isPresent()) {
                JobOpportunityDtoView jobOpportunityDtoView = convertEntityToDtoView(jobOpportunity.get());

                if(jobOpportunityDtoView.getJobPoster() != null && !jobOpportunityDtoView.getJobPoster().isEmpty()) {
                    String posterImage = getImagesPath()+jobOpportunityDtoView.getJobPoster();
                    jobOpportunityDtoView.setJobPoster(posterImage);
                }
                response.setSuccess(true);
                response.setMessage("Job opportunity with id "+jobOpportunityDtoView.getId()+" found");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(jobOpportunityDtoView);
            } else {
                response.setSuccess(false);
                response.setMessage("Opportunity with id "+jobId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
                log.debug(e.getMessage());
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public CustomResponse<List<JobOpportunityDtoView>> viewAllOpportunities() {
        CustomResponse<List<JobOpportunityDtoView>> response = new CustomResponse<>();
        try {
            List<JobOpportunityDtoView> jobOpportunities = jobOpportunityRepository.findAll().stream().map(this::convertEntityToDtoView).collect(Collectors.toList());
            if(jobOpportunities.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("No job opportunities found.");
                response.setPayload(jobOpportunities);
            } else {
                for(JobOpportunityDtoView job:jobOpportunities) {
                    if(job.getJobPoster() != null && !job.getJobPoster().isEmpty()) {
                        String jobPoster = getImagesPath()+job.getJobPoster();
                        job.setJobPoster(jobPoster);
                    }
                }

                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Found "+jobOpportunities.size()+" job opportunities");
                response.setPayload(jobOpportunities);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public CustomResponse<Long> countAllOpportunities() {
        CustomResponse<Long> response = new CustomResponse<>();
        try {
            Long opportunities = jobOpportunityRepository.count();
            response.setSuccess(true);
            response.setMessage("found "+opportunities+" job opportunities");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(opportunities);
        } catch (Exception e) {
            log.debug(e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<List<JobOpportunityDtoView>> viewActiveOpportunities() {
        CustomResponse<List<JobOpportunityDtoView>> response = new CustomResponse<>();
        LocalDateTime currentTime = LocalDateTime.now();

        try {
            List<JobOpportunityDtoView> opportunities = jobOpportunityRepository.findJobOpportunitiesByApplicationDeadLineIsBefore(currentTime).stream().map(this::convertEntityToDtoView).collect(Collectors.toList());

            if (opportunities.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("No active job opportunities available");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(opportunities);
            } else {
                for(JobOpportunityDtoView job:opportunities) {
                    if(job.getJobPoster() != null && !job.getJobPoster().isEmpty()) {
                        String jobPoster = getImagesPath()+job.getJobPoster();
                        job.setJobPoster(jobPoster);
                    }
                }

                response.setSuccess(true);
                response.setMessage(opportunities.size()+" active Job opportunities found");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(opportunities);
            }
        } catch (Exception e) {
                log.debug(e.getMessage());
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setMessage(e.getMessage());
        }
        return response;
    }


    @Override
    public CustomResponse<Long> countActiveOpportunities() {
        CustomResponse<Long> response = new CustomResponse<>();
        LocalDateTime specifiedTime = LocalDateTime.now();
        try {
            Long jobOpportunities = jobOpportunityRepository.countJobOpportunitiesByApplicationDeadLineIsBefore(specifiedTime);
            response.setSuccess(true);
            response.setMessage("Found "+jobOpportunities+" active job opportunities");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(jobOpportunities);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateJobOpportunityById(Long jobId, JobOpportunityDto jobOpportunityUpdate) throws NoResourceFoundException {
        CustomResponse<JobOpportunity> response = new CustomResponse<>();

        Optional<JobOpportunity> existingJobOpportunity = jobOpportunityRepository.findById(jobId);

        try {
            if(existingJobOpportunity.isPresent()) {
                JobOpportunity existingJob = existingJobOpportunity.get();

                if(existingJob.getApplicationDeadLine() != jobOpportunityUpdate.getApplicationDeadLine() || existingJob.getApplicationDeadLine() == null) {
                    existingJob.setApplicationDeadLine(jobOpportunityUpdate.getApplicationDeadLine());
                } else {
                    existingJob.setApplicationDeadLine(existingJob.getApplicationDeadLine());
                }

                if((!Objects.equals(existingJob.getJobTitle(), jobOpportunityUpdate.getJobTitle()) || existingJob.getJobTitle().isEmpty()) && !jobOpportunityUpdate.getJobTitle().isEmpty()) {
                    existingJob.setJobTitle(jobOpportunityUpdate.getJobTitle());
                } else {
                    existingJob.setJobTitle(existingJob.getJobTitle());
                }

                if(!Objects.equals(existingJob.getJobSalary(), jobOpportunityUpdate.getJobSalary()) || existingJob.getJobSalary() == null) {
                    existingJob.setJobSalary(jobOpportunityUpdate.getJobSalary());
                } else {
                    existingJob.setJobSalary(existingJob.getJobSalary());
                }



                jobOpportunityRepository.save(existingJob);

                response.setMessage("job with id: "+existingJob.getId()+" updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(existingJob);
            } else {
                response.setMessage("Job with id "+jobId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage());
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateJobOpportunityWithPosterById(Long jobId, JobOpportunityDto jobOpportunityWithPoster) throws NoResourceFoundException, IOException {
        CustomResponse<JobOpportunity> response = new CustomResponse<>();

        try {
            Optional<JobOpportunity> existingOpportunity = jobOpportunityRepository.findById(jobId);

            if(existingOpportunity.isPresent()) {
                JobOpportunity jobOpportunity = existingOpportunity.get();
                JobOpportunity updatedJobOpportunity = modelMapper.map(jobOpportunityWithPoster, JobOpportunity.class);
                log.info("updated job is: "+updatedJobOpportunity);
                // modelMapper.map(jobOpportunity,updatedJobOpportunity);
                
            if (jobOpportunityWithPoster.getJobPoster() != null && !jobOpportunityWithPoster.getJobPoster().isEmpty()) {
                String imageName = jobOpportunityWithPoster.getJobPoster().getOriginalFilename();
                assert imageName != null;
                String uniqueName = imageName+imageName.substring(imageName.lastIndexOf('.'));

                jobOpportunityWithPoster.getJobPoster().transferTo(uploadPath.resolve(uniqueName));
                jobOpportunity.setJobPoster(uniqueName);
                jobOpportunity.setOrganization(jobOpportunity.getOrganization());


                if(jobOpportunity.getApplicationDeadLine() != jobOpportunityWithPoster.getApplicationDeadLine() || jobOpportunity.getApplicationDeadLine() == null) {
                    jobOpportunity.setApplicationDeadLine(jobOpportunityWithPoster.getApplicationDeadLine());
                } else {
                    jobOpportunity.setApplicationDeadLine(jobOpportunity.getApplicationDeadLine());
                }

                if((!Objects.equals(jobOpportunity.getJobTitle(), jobOpportunityWithPoster.getJobTitle()) || jobOpportunity.getJobTitle().isEmpty()) && !jobOpportunityWithPoster.getJobTitle().isEmpty()) {
                    jobOpportunity.setJobTitle(jobOpportunityWithPoster.getJobTitle());
                } else {
                    jobOpportunity.setJobTitle(jobOpportunity.getJobTitle());
                }

                if(!Objects.equals(jobOpportunity.getJobSalary(), jobOpportunityWithPoster.getJobSalary()) || jobOpportunity.getJobSalary() == null) {
                    jobOpportunity.setJobSalary(jobOpportunityWithPoster.getJobSalary());
                } else {
                    jobOpportunity.setJobSalary(jobOpportunity.getJobSalary());
                }

                jobOpportunityRepository.save(jobOpportunity);


                response.setMessage("job with id "+jobId+" updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(jobOpportunity);
            }
            } else {
                response.setMessage("job with id "+jobId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_EXTENDED.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;

    }

    @Override
    public CustomResponse<?> deleteJobOpportunityById(Long jobId) throws NoResourceFoundException {
        CustomResponse<?> response = new CustomResponse<>();

        try {
            Optional<JobOpportunity> opportunityToDelete = jobOpportunityRepository.findById(jobId);

            if(opportunityToDelete.isPresent()) {
               jobOpportunityRepository.deleteById(jobId);
               
               response.setMessage("job opportunity with id: "+jobId+" removed successfully");
               response.setSuccess(true);
               response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("job opportunity with id: "+jobId+"does not exist");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public void notifyUsersAboutOpportunitiesWithPosters(JobOpportunity jobOpportunity) {
        List<Users> recipients = usersRepository.findAll();
        String subject = jobOpportunity.getJobTitle();
        String body = emailService.generateJobOpportunityBody(jobOpportunity);

        emailService.sendEmailWithAttachmentToRecipientsv3(recipients, subject, body, jobOpportunity.getJobPoster());
    }

    @Override
    public void notifyUsersAboutOpportunities(JobOpportunity jobOpportunity) {
        List<Users> recipients = usersRepository.findAll();
        String subject = jobOpportunity.getJobTitle();
        String body = emailService.generateJobOpportunityBody(jobOpportunity);
        for (Users recipient : recipients){
            String address = recipient.getUserEmail();
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(address);
            emailDetails.setSubject(subject);
            emailDetails.setMessageBody(body);

            emailService.sendWithOutAttachment(emailDetails);
        }
    }

    private JobOpportunityDtoView convertEntityToDtoView(JobOpportunity jobOpportunity) {
        return modelMapper.map(jobOpportunity, JobOpportunityDtoView.class);
    }



    public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            try {
                int port = serverPortService.getPort();

                if(port > 1023) {
                    imagesPath = hostNameCapture.getHost()+":"+port+"/images/";
                } else {
                    log.debug("Port is reserved for system use");
                }
                // imagesPath = hostNameCapture.getHost()+":5555/images/";
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagesPath;
    }

    @Override
    public CustomResponse<?> easyApply(Long userId, Long jobId) {
        CustomResponse<JobApplication> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUser = usersRepository.getUserById(userId);
            if (optionalUser.isEmpty()) {
                response.setMessage("User with ID " + userId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            Optional<JobOpportunity> optionalJobOpportunity = jobOpportunityRepository.findById(jobId);
            if (optionalJobOpportunity.isEmpty()) {
                response.setMessage("Job opportunity with ID " + jobId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            Users user = optionalUser.get();
            JobOpportunity jobOpportunity = optionalJobOpportunity.get();

            JobApplication jobApplication = new JobApplication();
            jobApplication.setUser(user);
            jobApplication.setJobOpportunity(jobOpportunity);
            jobApplication.setApplicationDate(LocalDate.now());

            jobApplication = jobApplicationRepository.save(jobApplication);

            response.setPayload(jobApplication);
            response.setMessage("Job application saved successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("Error saving job application: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<?> countJobApplications(Long opportunityId) {
        CustomResponse<Long> response = new CustomResponse<>();

        try {
            Optional<Long>optionalJobApplication=jobApplicationRepository.findByOpportunityId(opportunityId);
        if (optionalJobApplication.isPresent()){
            Long jobApplication=optionalJobApplication.get();
            response.setPayload(jobApplication);
            response.setMessage("found");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        }else {
            response.setPayload(null);
            response.setMessage("no record found");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(false);
        }
        } catch (Exception e) {
            response.setMessage("Error counting job applications: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<List<JobOpportunity>> searchOpportunity(String keyword) {
        CustomResponse<List<JobOpportunity>> response = new CustomResponse<>();

        try {
            // Assuming your repository has a method to search by keyword in various fields
            List<JobOpportunity> opportunities = jobOpportunityRepository.findByKeyword(keyword);

            if (opportunities.isEmpty()) {
                response.setMessage("No opportunities found matching the search criteria");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            for (JobOpportunity job : opportunities) {
                if (job.getJobPoster() != null && !job.getJobPoster().isEmpty()) {
                    String jobPoster = getImagesPath() + job.getJobPoster();
                    job.setJobPoster(jobPoster);
                }
            }

            response.setPayload(opportunities);
            response.setMessage("Found " + opportunities.size() + " opportunities matching the search criteria");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("Error searching for opportunities: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }




}
