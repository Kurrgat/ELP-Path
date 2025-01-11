package com.example.emtechelppathbackend.scholarcv;

import com.example.emtechelppathbackend.Career.Career;
import com.example.emtechelppathbackend.Career.CareerCvDto;
import com.example.emtechelppathbackend.Career.CareerDto;
import com.example.emtechelppathbackend.Career.CareerRepo;
import com.example.emtechelppathbackend.bio.Bio;
import com.example.emtechelppathbackend.bio.BioCvDto;
import com.example.emtechelppathbackend.bio.BioDto;
import com.example.emtechelppathbackend.bio.BioRepository;
import com.example.emtechelppathbackend.education.*;
import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.profile.ProfileDto;
import com.example.emtechelppathbackend.profile.ProfileRepo;
import com.example.emtechelppathbackend.profile.achievements.Achievements;
import com.example.emtechelppathbackend.profile.achievements.AchievementsDto;
import com.example.emtechelppathbackend.profile.achievements.AchievementsRepository;
import com.example.emtechelppathbackend.skills.*;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScholarCvServiceImpl implements ScholarCvService{

private final ProfileRepo  profileRepo;
private final ModelMapper modelMapper;
private final EducationRepo educationRepo;
private final BioRepository bioRepo;
private final CareerRepo careerRepo;
private final AchievementsRepository achievementsRepo;
private final SkillsRepository skillsRepo;
private final CourseRepo courseRepo;


    @Override
    public CustomResponse<ScholarCvDto> getCv(Long userId) {
        CustomResponse<ScholarCvDto> response = new CustomResponse<>();
        try {
            Optional<Profile> profileOptional = profileRepo.findByUserId(userId);
            List<Education> educationList = educationRepo.findByUserId(userId);
            Optional<Bio> bioOptional = bioRepo.findByUserId(userId);
            List<Achievements> achievementsList = achievementsRepo.findByUserId(userId);
            List<Career> careerList = careerRepo.findByUserId(userId);
            List<Skills> skillsList = skillsRepo.findByUserId(userId);

            if (profileOptional.isPresent()) {
                Profile profile = profileOptional.get();
                ScholarCvDto scholarCvDto = new ScholarCvDto();

                ProfileDto profileDto = modelMapper.map(profile, ProfileDto.class);
                scholarCvDto.setProfile(profileDto);

                List<EducationCvDto> educationDtoList = educationList.stream()
                        .map(education -> {
                            EducationCvDto educationDto = modelMapper.map(education, EducationCvDto.class);
                            educationDto.setInstitutionName(education.getInstitution().getName());

                            // Fetch and set course name using course ID
                            Course course = courseRepo.findById(education.getCourse().getId()).orElse(null);
                            if (course != null) {
                                educationDto.setCourse_name(course.getName());
                            }

                            return educationDto;
                        })
                        .collect(Collectors.toList());
                scholarCvDto.setEducation(educationDtoList);

                bioOptional.ifPresent(bio -> scholarCvDto.setBio(modelMapper.map(bio, BioCvDto.class)));

                List<AchievementsDto> achievementsDtoList = achievementsList.stream()
                        .map(achievements -> modelMapper.map(achievements, AchievementsDto.class))
                        .collect(Collectors.toList());
                scholarCvDto.setAchievements(achievementsDtoList);

                List<CareerCvDto> careerDtoList = careerList.stream()
                        .map(career -> modelMapper.map(career, CareerCvDto.class))
                        .collect(Collectors.toList());
                scholarCvDto.setCareer(careerDtoList);

                List<SkillsDto> skillsDtoList = skillsList.stream()
                        .map(skill -> modelMapper.map(skill, SkillsDto.class))
                        .collect(Collectors.toList());
                scholarCvDto.setSkills(skillsDtoList);

                List<SoftSkillsDto> softSkillsDtoList = skillsList.stream()
                        .map(skill -> modelMapper.map(skill, SoftSkillsDto.class))
                        .collect(Collectors.toList());
                scholarCvDto.setSoftSkills(softSkillsDtoList);

                List<SkillsLanguageDto> languageSkillsDtoList = skillsList.stream()
                        .map(skill -> modelMapper.map(skill, SkillsLanguageDto.class))
                        .collect(Collectors.toList());
                scholarCvDto.setLanguageSkills(languageSkillsDtoList);

                response.setPayload(scholarCvDto);
                response.setMessage("CV fetched successfully");
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("No profile found for userId: " + userId);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }

}
