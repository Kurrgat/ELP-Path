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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {
    private final CareerRepo careerRepo;
    private final UsersRepository userRepository;



    @Override
    public CustomResponse<Career> addCareer(CareerDto careerDto, Long userId) {
        CustomResponse<Career> response = new CustomResponse<>();
        try {
            Career career = mapToEntity(careerDto);
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserDetailsNotFoundException("User not found"));
            LocalDate currentDate = LocalDate.now();
            // Validate start date and end date if toDate is false
            if (Boolean.FALSE.equals(careerDto.getToDate()) && careerDto.getStart_date().isAfter(careerDto.getEnd_date())) {
                response.setMessage("Start date cannot be after end date");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }
            // Check if start date is in the future
            if (careerDto.getStart_date().isAfter(currentDate)) {
                response.setMessage("Start date cannot be in the future");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Set user and other attributes from DTO to the career entity
            career.setUser(user);
            career.setCompanyName(careerDto.getCompanyName());
            career.setDescription(careerDto.getDescription());
            career.setStart_date(careerDto.getStart_date());

            // Set end date based on the toDate flag
            if (Boolean.TRUE.equals(careerDto.getToDate())) {
                career.setEnd_date(null); // Set end date to zero
            } else {
                career.setEnd_date(careerDto.getEnd_date());
                career.setToDate(false);
            }
            career.setToDate(careerDto.getToDate());

            // Save the career entity to the database
            Career newCareer = careerRepo.save(career);

            // Set success response
            response.setPayload(newCareer);
            response.setMessage("Career added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("An error occurred while adding the career: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    private Career mapToEntity(CareerDto careerDto) {
        Career career = new Career();
        career.setCompanyName(careerDto.getCompanyName());
        career.setOrganizationSector(careerDto.getOrganizationSector());
        career.setPosition(careerDto.getPosition());
        career.setDescription(careerDto.getDescription());
        career.setCareerRole(careerDto.getCareerRole());
        career.setStart_date(careerDto.getStart_date());
        career.setEnd_date(careerDto.getEnd_date());
        career.setToDate(careerDto.getToDate());

        return career;
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

            // Validate start date and end date
            if (careerDto.getStart_date().isAfter(careerDto.getEnd_date())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }

            // Set end date based on the toDate flag
            if (Boolean.TRUE.equals(careerDto.getToDate())) {
                careerDto.setEnd_date(careerDto.getEnd_date()); // Set end date to zero
            }



            career.setCompanyName(careerDto.getCompanyName());
            career.setDescription(careerDto.getDescription());
            career.setStart_date(careerDto.getStart_date());
            career.setEnd_date(careerDto.getEnd_date());
            career.setCareerRole(careerDto.getCareerRole());
            career.setPosition(careerDto.getPosition());
            career.setOrganizationSector(careerDto.getOrganizationSector());
            career.setToDate(careerDto.getToDate());

            Career updatedCareer = careerRepo.save(career);

            response.setMessage("Career updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(updatedCareer);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value()); // You may adjust status codes accordingly
            response.setSuccess(false);
        }

        return response;
    }



    @Override
    public CustomResponse<?> deleteCareer(Long id, Long userId) {
        CustomResponse<Career> response = new CustomResponse<>();
        try {
            Optional<Career> existingCareer = careerRepo.findByIdAndUserId(id, userId);

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

    @Override
    public CustomResponse<?> updateToDateCareer(Long userId, Long careerId, UpdateCareerDto careerDto) {
        CustomResponse<Career> response = new CustomResponse<>();

        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserDetailsNotFoundException("User not found"));

            Career career = careerRepo.findById(careerId)
                    .orElseThrow(() -> new NotFoundException("Career not found"));

            if (!Objects.equals(career.getUser().getId(), user.getId())) {
                throw new NotFoundException("This career does not belong to this user");
            }

            // Assuming careerDto.getToDate() == null means the position is current/ongoing
            // Adjust this check according to how you determine the "to date" condition
            if(Boolean.TRUE.equals (careerDto.getToDate())) {
                career.setEnd_date(null);
            }

            career.setCompanyName(careerDto.getCompanyName());
            career.setDescription(careerDto.getDescription());
            career.setStart_date(careerDto.getStart_date());
            career.setCareerRole(careerDto.getCareerRole());
            career.setPosition(careerDto.getPosition());
            career.setOrganizationSector(careerDto.getOrganizationSector());
            career.setToDate(careerDto.getToDate());

            Career updatedCareer = careerRepo.save(career);

            response.setMessage("Career updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(updatedCareer);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value()); // Adjusting the status code as per your logic
            response.setSuccess(false);
        }

        return response;
    }


    private CareerDto mapToDto(Career career) {
        CareerDto careerDto = new CareerDto();
        careerDto.setId(career.getId());

        careerDto.setDescription(career.getDescription());
        careerDto.setStart_date(career.getStart_date());
        careerDto.setCareerRole(career.getCareerRole());
        careerDto.setPosition(career.getPosition());
        careerDto.setOrganizationSector(career.getOrganizationSector());
        careerDto.setCompanyName(career.getCompanyName());


        careerDto.setCompanyName(career.getCompanyName());
        careerDto.setEnd_date(career.getEnd_date());


        return careerDto;
    }
}