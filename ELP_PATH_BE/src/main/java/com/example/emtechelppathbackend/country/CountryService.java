package com.example.emtechelppathbackend.country;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {
    //CustomResponse<?> getCountries();
    List<Country> displayCountries();

    CustomResponse<?> fetchDialCodes();
}
