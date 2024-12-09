package com.example.emtechelppathbackend.country;

import com.example.emtechelppathbackend.scholars.ScholarDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;
    private final ModelMapper modelMapper;

    /*
    @GetMapping("/all")
    public ResponseEntity<?> getCountries() throws Exception {
        var response = countryService.getCountries();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    } */

    @GetMapping("/display-countries")
    public List<CountryDTO> displayScholars() throws Exception {
        return countryService.displayCountries()
                .stream().map(country -> modelMapper
                        .map(country, CountryDTO.class))
                .collect(Collectors.toList());
    }
}
