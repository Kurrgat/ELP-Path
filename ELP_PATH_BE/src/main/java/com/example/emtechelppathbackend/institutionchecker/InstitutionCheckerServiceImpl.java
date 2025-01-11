package com.example.emtechelppathbackend.institutionchecker;


import com.example.emtechelppathbackend.exceptions.ResourceNotFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstitutionCheckerServiceImpl implements InstitutionCheckerService{

    private final InstitutionCheckerRepository institutionCheckerRepository;
    @Override
    public CustomResponse<?> addInstitutionChecker(InstitutionChecker institutionChecker) {
        CustomResponse<InstitutionChecker> response = new CustomResponse<>();
        try {
            Optional<InstitutionChecker> optional = institutionCheckerRepository.findById(institutionChecker.getId());

            if (optional.isPresent()) {
                response.setPayload(null);
                response.setMessage("Institutional checker exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(true);

            } else {



                institutionCheckerRepository.save(institutionChecker);
                response.setPayload(institutionChecker);
                response.setMessage("added successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);

            }
        }catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }
}
