package com.example.emtechelppathbackend.Career;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/career")
public class  CareerController {
    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }
    @PostMapping("/{userId}/create")
    public ResponseEntity<?> addCareer(@PathVariable(value = "userId")Long userId, @RequestBody CareerDto careerDto){
        var response=careerService.addCareer(careerDto, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/{userId}/view")
    public ResponseEntity<?> viewCareerByUserId(@PathVariable Long userId){
        var response= careerService.viewCareerByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/user/{userId}/{careerId}/update")
    public ResponseEntity<?> updateCareer(@PathVariable(value = "userId")Long userId,@PathVariable(value = "careerId")Long careerId,@RequestBody CareerDto careerDto){
        var updatedCareer = careerService.updateCareer(userId, careerId, careerDto);
        return ResponseEntity.status(updatedCareer.getStatusCode()).body(updatedCareer);
    }
    @DeleteMapping("/{id}/{userId}/delete")
    public ResponseEntity<?>deleteCareer(@PathVariable Long id,@PathVariable Long userId){

       var response= careerService.deleteCareer( id, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/user/{userId}/{careerId}/to-date-update")
    public ResponseEntity<?> updateToDateCareer(@PathVariable(value = "userId")Long userId,@PathVariable(value = "careerId")Long careerId,@RequestBody UpdateCareerDto careerDto){
        var updatedCareer = careerService.updateToDateCareer(userId, careerId, careerDto);
        return ResponseEntity.status(updatedCareer.getStatusCode()).body(updatedCareer);
    }
}
