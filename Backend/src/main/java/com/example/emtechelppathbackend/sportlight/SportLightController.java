package com.example.emtechelppathbackend.sportlight;

import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/spotlight")
public class SportLightController {
    private final SportLightService sportLightService;
    @PostMapping("/spotlight-create")
    public ResponseEntity<?> addSportLight(@ModelAttribute SportLightDto sportLightDto){
          var response = sportLightService.addSportLight(sportLightDto);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        @GetMapping("/spotlight-all")
        public ResponseEntity<CustomResponse<List<SportLight>>> getAll(){
        var response= sportLightService.getAll();
        return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        @GetMapping("/{id}/spotlight")
        public ResponseEntity<?> getById(Long id){
        var response= sportLightService.getById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    @PutMapping("/{id}/spotlight")
    public ResponseEntity<?> updateSpotLight(@PathVariable Long id, @ModelAttribute  SportLightDto sportLightDto) {
        var response = sportLightService.updateSportLight(id, sportLightDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSportLightById(@PathVariable Long id) {
       var response= sportLightService.deleteSportLightById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




    }


