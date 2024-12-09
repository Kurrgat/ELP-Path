package com.example.emtechelppathbackend.profile.profiletracker;

import java.util.List;
import java.util.Optional;

import com.example.emtechelppathbackend.Career.Career;
import com.example.emtechelppathbackend.Career.CareerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.emtechelppathbackend.bio.Bio;
import com.example.emtechelppathbackend.bio.BioRepository;

import com.example.emtechelppathbackend.education.Education;
import com.example.emtechelppathbackend.education.EducationRepo;
import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.profile.ProfileRepo;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.skills.Skills;
import com.example.emtechelppathbackend.skills.SkillsRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;


import lombok.RequiredArgsConstructor;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileProgressService {
    private final UsersRepository usersRepository;
    private final EducationRepo educationRepo;
    private final CareerRepo careerRepo;
    private final SkillsRepository skillsRepository;
    private final BioRepository bioRepository;
    private final ProfileRepo profileRepo;


    public CustomResponse<ProgressResponse> getProfileUpdateStatus(Long userId) {
        CustomResponse<ProgressResponse> response = new CustomResponse<>();
        int progressPercentage;

        try {
            System.out.println(userId);
            Optional<Users> optional = usersRepository.findById(userId);

            if(optional.isEmpty()) {
                response.setMessage("User with id "+userId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else if(!(optional.get().getRole().getRoleName()).equals("SUPER_ADMIN")) {
                int profilePerc = calculateProfileCompletionPercentage(userId).getPayload().percentageDone;
                int bioPerc = calculateBioCompletionPercentage(userId).getPayload().percentageDone;
                int educationPerc = calculateEducationCompletionPercentage(userId).getPayload().percentageDone;
                int skillsPerc = calculateSkillsCompletionPercentage(userId).getPayload().percentageDone;
                int careerPerc = calculateCareerCompletionPercentage(userId).getPayload().percentageDone;

                progressPercentage = (profilePerc+bioPerc+educationPerc+skillsPerc+careerPerc)/5;

                ProgressResponse profileProgress = new ProgressResponse();
                profileProgress.setMessage(progressPercentage+"% of profile update done");
                profileProgress.setPercentage(progressPercentage);

                profileProgress.setBioTracker(calculateBioCompletionPercentage(userId).getPayload());
                profileProgress.setProfileTracker(calculateProfileCompletionPercentage(userId).getPayload());
                profileProgress.setEducationTracker(calculateEducationCompletionPercentage(userId).getPayload());
                profileProgress.setSkillsTracker(calculateSkillsCompletionPercentage(userId).getPayload());
                profileProgress.setCareerTracker(calculateCareerCompletionPercentage(userId).getPayload());

                response.setMessage(HttpStatus.OK.getReasonPhrase());
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(profileProgress);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }    

    public CustomResponse<ProfileTracker> calculateProfileCompletionPercentage(Long userId) {
        CustomResponse<ProfileTracker> response = new CustomResponse<>();
        ProfileTracker progressResponse = new ProfileTracker();
        ArrayList<String> pending = new ArrayList<>();
        int totalAttributes = 7; // Total number of attributes in the class

        int addedValues = 0;

        try {
            Profile userProfile = profileRepo.findProfileByUserId(userId);
            
            if(userProfile != null) {
                if (userProfile.getEmail() != null && !userProfile.getEmail().isEmpty()) {
                    addedValues++;
                } else {
                    pending.add("Kindly update your email");
                }

                if (userProfile.getTitle() != null && !userProfile.getTitle().isEmpty()) {
                    addedValues++;
                    System.out.println("user title is :"+userProfile.getTitle());
                } else {
                    pending.add("Kindly update your profile title");
                }

                if (userProfile.getPhoneNo() != null) {
                    addedValues++;
                } else {
                    pending.add("Update your phone no");
                }

                if (userProfile.getCurrentCountryofResidence() != null && !userProfile.getCurrentCountryofResidence().isEmpty()) {
                    addedValues++;
                    System.out.println("country"+userProfile.getCurrentCountryofResidence());
                } else {
                    pending.add("Update your country of residence");
                }


                if (userProfile.getCurrentCityofResidence() != null && !userProfile.getCurrentCityofResidence().isEmpty()) {
                    addedValues++;
                } else {
                    pending.add("Update your city of residence");
                }

                if (userProfile.getJobStatus() != null) {
                    addedValues++;
                    System.out.println("job status is :"+userProfile.getJobStatus());
                } else {
                    pending.add("Update your job status");
                }

                if (userProfile.getProfileImage() != null && !userProfile.getProfileImage().isEmpty()) {
                    addedValues++;
                } else {
                    pending.add("Update your profile image");
                }

                // Calculate the percentage
                if (addedValues == totalAttributes) {
                    // progressResponse.setMessage("Personal Details Upto Date");
                    progressResponse.setPercentageDone(100);
                } else {
                    progressResponse.setPercentageDone((addedValues * 100) / totalAttributes);
                }
                progressResponse.setPending(pending);
            } else {
                pending.add("No profile details yet");
                progressResponse.setPercentageDone(0);
            }

                response.setMessage(HttpStatus.OK.getReasonPhrase());
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(progressResponse);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
}

    public CustomResponse<EducationTracker> calculateEducationCompletionPercentage(Long userId) {
        CustomResponse<EducationTracker> response = new CustomResponse<>();
        EducationTracker progressResponse = new EducationTracker();
        ArrayList<String> pending = new ArrayList<>();
        int totalAttributes = 1; // Total number of attributes in the class

        int addedValues = 0;

        try {
            Optional<Education> education = educationRepo.findById(userId);

            if(education != null) {
                if (education.isPresent()) {
                    addedValues++;

                    if(addedValues == totalAttributes) {
                        // progressResponse.setMessage("Education details are upto date");
                        progressResponse.setPercentageDone(100);
                    } else {
                        pending.add("Kindly update your education details");
                        progressResponse.setPercentageDone((addedValues * 100) / totalAttributes);
                    }
                } else {
                    pending.add("Kindly update your education details");

                    progressResponse.setPercentageDone(0);
                    progressResponse.setPending(pending);

                    response.setMessage("No education details yet");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setPayload(progressResponse);
                    return response;
                }
            }
            
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(progressResponse);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
}

    public CustomResponse<CareerTracker> calculateCareerCompletionPercentage(Long userId) {
        CustomResponse<CareerTracker> response = new CustomResponse<>();
        CareerTracker progressResponse = new CareerTracker();
        ArrayList<String> pending = new ArrayList<>();
        int totalAttributes = 1; // Total number of attributes in the class

        int addedValues = 0;

        try {
            List<Career> careers = careerRepo.findByUserId(userId);

            if(careers != null) {
                if (careers.size() > 0) {
                    addedValues++;

                    if(addedValues == totalAttributes) {
                        // progressResponse.setMessage("Career Details Upto Date");
                        progressResponse.setPercentageDone(100);
                    } else {
                        progressResponse.setPercentageDone((addedValues * 100) / totalAttributes);
                    }
                } else {
                    pending.add("Kindly update your career details");
                }
            } else {
                pending.add("User hasn't added any career details yet");
                progressResponse.setPercentageDone(0);

                response.setMessage("Add your career details");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(progressResponse);
                return response;
            }

            progressResponse.setPending(pending);
            
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(progressResponse);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
}

    public CustomResponse<SkillsTracker> calculateSkillsCompletionPercentage(Long userId) {
        CustomResponse<SkillsTracker> response = new CustomResponse<>();
        SkillsTracker progressResponse = new SkillsTracker();
        ArrayList<String> pending = new ArrayList<>();
        int totalAttributes = 1; 

        int addedValues = 0;

        try {

            if(!skillsRepository.findAll().isEmpty()) {
                List<Skills> skills = skillsRepository.findByUserId(userId);
                if (!skills.isEmpty()) {
                    addedValues++;
                } else {
                    pending.add("Update skills details");
                    progressResponse.setPercentageDone(0);
                }
                
            } else {
                pending.add("Include your skills");
                progressResponse.setPercentageDone(0);

                System.out.println("Skills details: "+progressResponse);
            }

            if (addedValues == totalAttributes) {
                // progressResponse.setMessage("Skills Details Upto Date");
                progressResponse.setPercentageDone(100);
            } else {
                progressResponse.setPercentageDone((addedValues * 100) / totalAttributes);
            }
            progressResponse.setPending(pending);
            
            
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(progressResponse);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
}

public CustomResponse<BioTracker> calculateBioCompletionPercentage(Long userId) {
        CustomResponse<BioTracker> response = new CustomResponse<>();
        BioTracker progressResponse = new BioTracker();
        ArrayList<String> pending = new ArrayList<>();
        int totalAttributes = 1; 

        int addedValues = 0;

        try {
            Bio bio = bioRepository.findBioByUserId(userId);

            if (bio != null) {
                if (bio.getUser() != null) {
                    addedValues++;
                if (addedValues == totalAttributes) {
                    // progressResponse.setMessage("Bio Details Upto Date");
                    progressResponse.setPercentageDone(100);
                } else {
                    progressResponse.setPercentageDone((addedValues * 100) / totalAttributes);
                }
                } else {
                    pending.add("Add your bio details");
                    progressResponse.setPercentageDone(0);
                }
            } else {
                pending.add("Bio details");
                progressResponse.setPercentageDone(0);

            }
            progressResponse.setPending(pending);

            
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(progressResponse);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
}
}