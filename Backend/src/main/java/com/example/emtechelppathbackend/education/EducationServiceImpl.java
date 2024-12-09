package com.example.emtechelppathbackend.education;


import com.example.emtechelppathbackend.exceptions.ResourceNotFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.security.user.UserService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

record CombinedData( CourseClusterInterface courseCluster, List<CourseInterface> courses) {}

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final EducationRepo educationRepo;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final InstitutionRepo institutionRepo;
    private final CourseRepo courseRepo;


    @Override
    public CustomResponse<?> addEducation(AddEducationDTO addEducationDTO, Long userId, Long courseId, Long institutionId) {
        CustomResponse<List<Education>> response = new CustomResponse<>();
        Education education = new Education();

        try {
            Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with that id not found"));

            education.setUser(users);
            education.setYearOfStudy(addEducationDTO.getYearOfStudy());
            education.setOngoing(addEducationDTO.getOngoing());
            education.setSemester(addEducationDTO.getSemester());
            education.setStart_date(addEducationDTO.getStartDate());

            // Check if education is ongoing
            if (addEducationDTO.getOngoing()) {
                // If ongoing, the end date should not be set
                education.setEnd_date(null);
            } else {

                education.setEnd_date(addEducationDTO.getEndDate());

            }

            Optional<Institution> optionalInstitution = institutionRepo.findById(institutionId);
            Optional<Course> optionalCourse = courseRepo.findById(courseId);



            if (optionalInstitution.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Institution with id " + institutionId + " not found");
                response.setSuccess(false);
            } else {
                Institution institution = optionalInstitution.get();
                education.setInstitution(institution);
            }



            if (optionalCourse.isEmpty()) {
                response.setMessage("Course with id " + courseId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Course course = optionalCourse.get();
                education.setCourse(course);
            }

            System.out.println("education data: " + education);

            educationRepo.save(education);

            var result = educationRepo.findByUserId(userId);

            response.setMessage("Education details added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(result);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
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



    //Not working getting constrain validation error
    @Override
    public Education updateEducation(Long userId, Long educationId, Long courseId, Long institutionId, AddEducationDTO addEducationDTO) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("user not found"));
        Education education = educationRepo.findById(educationId).orElseThrow(() -> new UserDetailsNotFoundException("education for this user not found"));
        if (!Objects.equals(education.getUser().getId(), users.getId())) {
            throw new UserDetailsNotFoundException("this education details do not belong to this user");
        }

        Institution institution = institutionRepo.findById(institutionId).orElseThrow(() -> new ResourceNotFoundException("Institution", "id", institutionId));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course ", "id", courseId));


        education.setStart_date(addEducationDTO.getStartDate());
        education.setEnd_date(addEducationDTO.getEndDate());
        education.setCourse(course);
        education.setInstitution(institution);

        return educationRepo.save(education);
    }

    @Override
    public Education updateUserEducation(Long userId, Long educationId, Long courseId, Long institutionId, AddEducationDTO addEducationDTO) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailsNotFoundException("User not found"));

        Education education = educationRepo.findById(educationId)
                .orElseThrow(() -> new UserDetailsNotFoundException("Education not found"));

        if (!Objects.equals(education.getUser().getId(), user.getId())) {
            throw new UserDetailsNotFoundException("This education does not belong to this user");
        }

        // Update the education fields
        Institution institution = institutionRepo.findById(institutionId).orElseThrow(() -> new ResourceNotFoundException("Institution", "id", institutionId));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course ", "id", courseId));


        education.setStart_date(addEducationDTO.getStartDate());
        education.setEnd_date(addEducationDTO.getEndDate());
        education.setCourse(course);
        education.setInstitution(institution);

        // Save the updated education
        var result = educationRepo.save(education);
        return result;
    }


    @Override
    public void deleteEducation(Long userId, Long educationId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        Education education = educationRepo.findById(educationId).orElseThrow(() -> new RuntimeException("education for this user not found"));
        if (!Objects.equals(education.getUser().getId(), users.getId())) {
            throw new RuntimeException("this education details do not belong to this user");
        }
        educationRepo.delete(education);
    }

    @Override
    public CustomResponse<?> fetchInstitutions() {
        CustomResponse<List<Institution>> response = new CustomResponse<>();
        try {
            List<Institution> institutions = institutionRepo.findAll();
            if (institutions.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No institution found");
                response.setPayload(null);
            }else {
                response.setPayload(institutions);
                response.setMessage("Found");
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
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


    private EducationDto mapToDto(Education education) {
        EducationDto educationDto = new EducationDto();
        educationDto.setId(education.getId());

        educationDto.setInstitution(education.getInstitution());
        educationDto.setStart_date(education.getStart_date());
        educationDto.setEnd_date(education.getEnd_date());
        educationDto.setYearOfStudy(education.getYearOfStudy());

        educationDto.setOngoing(education.getOngoing());
        educationDto.setSemester(education.getSemester());
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

        education.setStart_date(educationDto.getStart_date());
        education.setEnd_date(educationDto.getEnd_date());

        UsersDto usersDto = educationDto.getUser();
        if (usersDto != null) {
            Users userEntity = userService.mapUserDtoToEntity(usersDto);
            education.setUser(userEntity);
        }

        return education;
    }
}
