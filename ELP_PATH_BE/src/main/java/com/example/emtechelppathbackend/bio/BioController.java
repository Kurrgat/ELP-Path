package com.example.emtechelppathbackend.bio;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bio")
public class BioController {
    private final BioService bioService;

    public BioController(BioService bioService) {
        this.bioService = bioService;
    }
    @GetMapping("/{userId}/view")
    public ResponseEntity<CustomResponse<BioDto>> viewBio(@PathVariable(value = "userId")Long userId){

        var response= bioService.findBioByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/{userId}/add")
    public ResponseEntity<BioDto> createBio(@PathVariable(value = "userId")Long userId,@RequestBody BioDto bioDto){
        return new ResponseEntity<>(bioService.addBio(userId,bioDto), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/{bioId}/update")
    public ResponseEntity<?> updateBio(@PathVariable(value = "userId")Long userId,@PathVariable(value = "bioId")Long bioId,@RequestBody BioDto bioDto){
        var response  = bioService.updateBioByUserId(userId, bioId, bioDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{userId}/{bioId}/delete")
    public ResponseEntity<?>deleteBio(@PathVariable (value = "userId")Long userId,@PathVariable(value = "bioId")Long bioId){
       var response= bioService.deleteBio(userId, bioId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
