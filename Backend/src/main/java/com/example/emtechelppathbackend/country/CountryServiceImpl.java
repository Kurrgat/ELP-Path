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


    /*
    @Override
    public CustomResponse<?> getCountries(){
        CustomResponse<List<Country>> response = new CustomResponse<>();

        try{
            List<Country> countries = countryRepo.findAll();
            if (countries.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setMessage("No countries available");
            }else {
                response.setMessage("Found");
                response.setPayload(countries);
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    } */

    @Override
    public List<Country> displayCountries(){
        return countryRepo.findAll();
    }
}

