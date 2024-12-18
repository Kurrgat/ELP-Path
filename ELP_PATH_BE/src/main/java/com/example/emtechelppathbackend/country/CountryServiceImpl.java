package com.example.emtechelppathbackend.country;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{
    private final CountryRepo countryRepo;




    @Override
    public List<Country> displayCountries(){
        return countryRepo.findAll();
    }

    @Override
    public CustomResponse<?> fetchDialCodes() {
        CustomResponse<List<CountryRepo.CountryInterface>> response = new CustomResponse<>();
        try {
            List<CountryRepo.CountryInterface> countryCodes = countryRepo.findAllCountryCodes();

            if (countryCodes.isEmpty()) {
                response.setMessage("No dial codes found");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(null);
            } else {
                response.setMessage("Dial codes fetched successfully");
                response.setSuccess(true);
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(countryCodes);
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }
        return response;
    }

}

