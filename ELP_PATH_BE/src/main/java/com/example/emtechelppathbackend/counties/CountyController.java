package com.example.emtechelppathbackend.counties;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counties")
public class CountyController {
    private final CountyService countyService;

    @GetMapping("/all")
    public ResponseEntity<?> fetchKenyanCounties(){
        var response = countyService.fetchKenyanCounties();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
