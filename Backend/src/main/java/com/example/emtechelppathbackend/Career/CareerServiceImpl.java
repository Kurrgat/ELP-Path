package com.example.emtechelppathbackend.Career;

import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.security.user.UserService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {
    private final CareerRepo careerRepo;
    private final UsersRepository userRepository;
    private final UserService userService;


    @Override
    public CareerDto addSkills(CareerDto careerDto, Long userId) {
        Career career = mapToEntity(careerDto);
        Users users = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        career.setUser(users);
        career.setCompanyName(careerDto.getCompanyName());
        career.setDescription(careerDto.getDescription());
        career.setStart_date(careerDto.getStart_date());
        career.setEnd_date(careerDto.getEnd_date());
        Career newCareer = careerRepo.save(career);
        return mapToDto(newCareer);
    }

    @Override
    public CustomResponse<?> viewCareerByUserId(Long userId) {
        CustomResponse<List<CareerDto>> response = new CustomResponse<>();
        try {
            // Assuming you have an active status column in the Career entity, replace 'active' with your actual column name
            List<Career> careers = careerRepo.findByUserId(userId);

            if (careers.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No career profile found for the specified user");
                response.setPayload(null);
            } else {
                List<CareerDto> total = careers.stream().map(this::mapToDto).collect(Collectors.toList());
                response.setPayload(total);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateCareer(Long userId, Long careerId, CareerDto careerDto) {
        CustomResponse<Career> response = new CustomResponse<>();

        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserDetailsNotFoundException("User not found"));

            Career career = careerRepo.findById(careerId)
                    .orElseThrow(() -> new NotFoundException("Career not found"));

            if (!Objects.equals(career.getUser().getId(), user.getId())) {
                throw new NotFoundException("This career does not belong to this user");
            }

            career.setTitle(careerDto.getTitle());
            career.setCompanyName(careerDto.getCompanyName());
            career.setDescription(careerDto.getDescription());
            career.setStart_date(careerDto.getStart_date());
            career.setEnd_date(careerDto.getEnd_date());

            Career updatedCareer = careerRepo.save(career);

            response.setMessage("Career updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(updatedCareer);
        } catch ( Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value()); // You may adjust status codes accordingly
            response.setSuccess(false);
        }

        return response;
    }




    @Override
    public CustomResponse<?> deleteCareerById(Long id) {
        CustomResponse<Career> response = new CustomResponse<>();
        try {
            Optional<Career> existingCareer = careerRepo.findById(id);

            if (existingCareer.isPresent()) {

                    careerRepo.deleteById(id);
                    response.setMessage("Career deleted successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                }
             else {
                response.setMessage("Career not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Failed to delete career: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }


    private CareerDto mapToDto(Career career){
        CareerDto careerDto = new CareerDto();
        careerDto.setId(career.getId());
        careerDto.setTitle(career.getTitle());
        careerDto.setDescription(career.getDescription());
        careerDto.setStart_date(career.getStart_date());
        careerDto.setEnd_date(career.getEnd_date());
        careerDto.setCompanyName(career.getCompanyName());
        Users usersEntity = career.getUser();
        if (usersEntity!=null){
            UsersDto usersDto = userService.mapUserToDto(usersEntity);
            careerDto.setUser(usersDto);
        }
//        careerDto.setUser(userService.mapUserToDto(career.getUser()));
        return careerDto;
    }
    private Career mapToEntity(CareerDto careerDto){
        Career career = new Career();
        career.setId(careerDto.getId());
        career.setTitle(careerDto.getTitle());
        career.setDescription(careerDto.getDescription());
        career.setStart_date(careerDto.getStart_date());
        career.setEnd_date(careerDto.getEnd_date());
        UsersDto usersDto = careerDto.getUser();
        if (usersDto != null){
            Users usersEntity = userService.mapUserDtoToEntity(usersDto);
            career.setUser(usersEntity);
        }
        return career;
    }
}
