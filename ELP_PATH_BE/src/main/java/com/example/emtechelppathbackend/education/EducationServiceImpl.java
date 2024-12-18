package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.chapter.ChapterRepoV2;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberRepositoryv2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.country.Country;
import com.example.emtechelppathbackend.country.CountryRepo;
import com.example.emtechelppathbackend.exceptions.ResourceNotFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.institutionchecker.InstitutionChecker;
import com.example.emtechelppathbackend.institutionchecker.InstitutionCheckerRepository;
import com.example.emtechelppathbackend.security.user.UserService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;


record CombinedData( CourseClusterInterface courseCluster, List<CourseInterface> courses) {}

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationServiceImpl implements EducationService {
    private final EducationRepo educationRepo;
    private final UsersRepository userRepository;
    private final UserService userService;
    private final InstitutionRepo institutionRepo;
    private final CourseRepo courseRepo;
    private final ChapterMemberRepositoryv2 chapterMemberRepository;
    private final ChapterRepoV2 chapterRepo;
    private final CountryRepo countryRepo;
    private final InstitutionCheckerRepository institutionCheckerRepository;

    @Override
    public CustomResponse<Education> addEducation(AddEducationDTO addEducationDTO, Long userId, Long courseId, Long institutionId, Long countryId) {
        CustomResponse<Education> response = new CustomResponse<>();
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

            Institution institution = institutionRepo.findById(institutionId)
                    .orElseThrow(() -> new NotFoundException("Institution with id " + institutionId + " not found"));

            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new NotFoundException("Course with id " + courseId + " not found"));

            Country country = countryRepo.findById(countryId)
                    .orElseThrow(() -> new NotFoundException("Country with id " + countryId + " not found"));

            Education education = createEducation(addEducationDTO, user, institution, course, country);
            educationRepo.save(education);
            assignToChapter(user, institution, education);

            response.setMessage("Education details added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(education);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    private Education createEducation(AddEducationDTO dto, Users user, Institution institution, Course course, Country country) {
        Education education = new Education();
        education.setUser(user);
        education.setCourse(course);
        education.setInstitution(institution);
        education.setCountry(country);
        education.setCourseLevel(dto.getCourseLevel());
        education.setEducationType(dto.getEducationType());
        education.setStartYear(dto.getStartYear());
        education.setGraduationYear(dto.getGraduationYear());
        education.setGrade(dto.getGrade());
        education.setExpectedGraduationYear(dto.getExpectedGraduationYear());
        return education;
    }

    private void assignToChapter(Users user, Institution institution, Education education) {
        ChapterMemberV2 chapterMember = new ChapterMemberV2();
        chapterMember.setMember(user);
        chapterMember.setJoiningDate(LocalDateTime.now());
        chapterMember.setActiveMembership(true);
        chapterMember.setEducation(education);

        Optional<InstitutionChecker> checker = findInstitutionChecker(institution.getCategory());
        if (checker.isPresent()) {
            chapterMember.setInstitutionChecker(checker.get());
        } else {
            // Assign to a default chapter if specific checker is not found
            Optional<ChapterV2> defaultChapter = chapterRepo.findByInstitutionId(institution.getId());
            defaultChapter.ifPresent(chapterMember::setChapter);
        }

        chapterMemberRepository.save(chapterMember);
    }

    private Optional<InstitutionChecker> findInstitutionChecker(String category) {
        switch (category) {
            case "GLOBAL_UNIVERSITY":
                return institutionCheckerRepository.findByNickName("Global Universities Chapter");
            case "REGIONAL_UNIVERSITY":
                return institutionCheckerRepository.findByNickName("Regional Universities Chapter");
            default:
                return institutionCheckerRepository.findByNickName("Other Universities Chapters");
        }
    }

    @Override
    public CustomResponse<?> updateEducation(Long userId, Long id, Long courseId, Long institutionId,Long countryId, AddEducationDTO addEducationDTO) {
        CustomResponse<Education> response = new CustomResponse<>();
        try {
            Users user = userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("User not found"));
            Education education = educationRepo.findById(id).orElseThrow(() -> new UserDetailsNotFoundException("Education for this user not found"));

            if (!Objects.equals(education.getUser().getId(), user.getId())) {
                throw new UserDetailsNotFoundException("This education details do not belong to this user");
            }

            Institution institution = institutionRepo.findById(institutionId).orElseThrow(() -> new ResourceNotFoundException("Institution", "id", institutionId));
            Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
            Country country=countryRepo.findById(countryId).orElseThrow(() -> new ResourceNotFoundException("Country", "id", countryId));
            // Update the education details
            education.setCourse(course);
            education.setInstitution(institution);
            education.setCourseLevel(addEducationDTO.getCourseLevel());
            education.setStartYear(addEducationDTO.getStartYear());
            education.setGraduationYear(addEducationDTO.getGraduationYear());
            education.setEducationType(addEducationDTO.getEducationType());
            education.setGrade(addEducationDTO.getGrade());
            education.setCountry(country);
            education.setExpectedGraduationYear(addEducationDTO.getExpectedGraduationYear());

            // Save the updated education
            Education updatedEducation = educationRepo.save(education);

            List<ChapterMemberV2> chapterMemberships = chapterMemberRepository.findByEducationId(education.getId());
            for (ChapterMemberV2 chapterMembership : chapterMemberships) {
                // Find the institutional chapter for the updated education's institution
                ChapterV2 institutionalChapter = chapterRepo.findByInstitution(institution.getId());
                if (institutionalChapter != null) {
                    // Set the chapter for the chapter membership
                    chapterMembership.setChapter(institutionalChapter);
                    // Update any other fields if needed
                }
            }
            chapterMemberRepository.saveAll(chapterMemberships);

            response.setPayload(updatedEducation);
            response.setMessage("Education updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (UserDetailsNotFoundException | ResourceNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<List<Education>> getEducationByUserId(Long userId) {
        CustomResponse<List<Education>> response = new CustomResponse<>();
        try {
            List<Education> educations = educationRepo.findByUserId(userId);
            var edu= educations.stream().map(this::mapToDto).toList();
            if (educations.isEmpty()) {

                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setPayload(educations);
            } else {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
                response.setPayload(educations);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }







    @Override
    public CustomResponse<?> deleteEducation(Long userId, Long educationId) {
        CustomResponse<Education> response = new CustomResponse<>();

        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Education education = educationRepo.findById(educationId)
                    .orElseThrow(() -> new RuntimeException("Education for this user not found"));

            if (!Objects.equals(education.getUser().getId(), user.getId())) {
                response.setMessage("This education does not belong to this user");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Find associated chapter memberships for the education
            List<ChapterMemberV2> chapterMemberships = chapterMemberRepository.findByUserIdAndEducationId(userId,educationId);

            // Delete associated chapter memberships
            if (!chapterMemberships.isEmpty()) {
                chapterMemberRepository.deleteAll(chapterMemberships);
            }

            // Now, delete the education itself
            educationRepo.delete(education);

            response.setMessage("Education and associated chapter memberships deleted successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("An error occurred while deleting education: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<?> fetchInstitutions(String countryName) {
        CustomResponse<List<InstitutionRepo.InstitutionInterface>> response = new CustomResponse<>();
        try {
            List<InstitutionRepo.InstitutionInterface> institutions = institutionRepo.findByCountryName(countryName);


            if (institutions.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No institution found for country Name: " + countryName);
                response.setPayload(null);
            } else {
                response.setPayload(institutions);
                response.setMessage("Institutions found for country Name: " + countryName);
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<?> findUniversityById(Long id) {
        CustomResponse<Institution> response = new CustomResponse<>();
        try {
            Optional<Institution> institution = institutionRepo.findById(id);
            if (institution.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No institution found");
                response.setPayload(null);
            }else {
                response.setPayload(institution.get());
                response.setMessage("Found");
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> fetchCourses() {
        CustomResponse<List<CourseInterface>> response = new  CustomResponse<>();
        try {
            List<CourseInterface> courses = educationRepo.fetchCourses();
            if (courses.isEmpty()){
                response.setMessage("No courses");
                response.setPayload(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }else {
                response.setPayload(courses);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public CustomResponse<?> fetchCourseClusters() {
        CustomResponse<List<CourseClusterInterface>> response = new  CustomResponse<>();
        try {
            List<CourseClusterInterface> courseClusters = educationRepo.fetchCourseClusters();
            if (courseClusters.isEmpty()){
                response.setMessage("No course cluster");
                response.setPayload(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }else {
                response.setPayload(courseClusters);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public CustomResponse<?> findCourseClusterById(Long clusterID) {
        CustomResponse<Object> response = new  CustomResponse<>();
        try {
            Optional<CourseClusterInterface> courseCluster = educationRepo.findCourseClusterById(clusterID);
            if (courseCluster.isEmpty()){
                response.setMessage("No course cluster");
                response.setPayload(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }else {
                List<CourseInterface> courses = educationRepo.fetchCoursesByCluster(clusterID);

                CombinedData data =  new CombinedData(courseCluster.get(), courses);
                response.setPayload(data);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public CustomResponse<?> findCourseById(Long courseId) {
        CustomResponse<CourseInterface> response = new  CustomResponse<>();
        try {
            Optional<CourseInterface> course = educationRepo.findCourseById(courseId);
            if (course.isEmpty()){
                response.setMessage("No course");
                response.setPayload(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }else {
                response.setPayload(course.get());
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getCourseClusters() {
        CustomResponse<List<EducationRepo.CourseClusterCount>> response = new CustomResponse<>();

        try {
            List<EducationRepo.CourseClusterCount> clusterCounts = educationRepo.countCourseClusters();

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setSuccess(true);
            response.setPayload(clusterCounts);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getEducationLevelCounts() {
        CustomResponse<List<EducationRepo.CourseLevelInterface>> response = new CustomResponse<>();

        try {
            List<EducationRepo.CourseLevelInterface> courseLevelCounts = educationRepo.findCountByCourseLevel();

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setSuccess(true);
            response.setPayload(courseLevelCounts);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> fetchByEducationLevel(String educationLevel) {
        CustomResponse<List<EducationRepo.EducationLevel>> response = new CustomResponse<>();

        try {
            List<EducationRepo.EducationLevel> courseLevelCounts = educationRepo.findByCourseLevel(educationLevel);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setSuccess(true);
            response.setPayload(courseLevelCounts);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }


    private EducationDto mapToDto(Education education) {
        EducationDto educationDto = new EducationDto();
        educationDto.setId(education.getId());

        educationDto.setInstitution(education.getInstitution());
//        educationDto.setStart_date(education.getStart_date());
//        educationDto.setEnd_date(education.getEnd_date());
//        educationDto.setYearOfStudy(education.getYearOfStudy());
//
//        //educationDto.setOngoing(education.getOngoing());
//        educationDto.setSemester(education.getSemester());
        educationDto.setUserCourse(education.getCourse());
        Users usersEntity = education.getUser();
        if (usersEntity != null) {
            UsersDto usersDto = userService.mapUserToDto(usersEntity);
            educationDto.setUser(usersDto);
        }
//        educationDto.setUser(userService.mapUserToDto(education.getUser()));
        return educationDto;
    }

    private Education mapToEntity(EducationDto educationDto) {
        Education education = new Education();
        education.setId(educationDto.getId());
        educationDto.setInstitution(education.getInstitution());
        educationDto.setUserCourse(education.getCourse());

//        education.setStart_date(educationDto.getStart_date());
//        education.setEnd_date(educationDto.getEnd_date());

        UsersDto usersDto = educationDto.getUser();
        if (usersDto != null) {
            Users userEntity = userService.mapUserDtoToEntity(usersDto);
            education.setUser(userEntity);
        }

        return education;
    }
}
