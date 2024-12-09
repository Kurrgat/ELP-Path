package com.example.emtechelppathbackend.newsandupdates;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/newsAndUpdates")

public class NewsAndUpdatesController {

    private  final NewsAndUpdatesService newsAndUpdatesService;
    @PostMapping("/create")
    public ResponseEntity<?> addNewsAndUpdates(@ModelAttribute NewsAndUpdatesDto newsAndUpdatesDto){

        var response = newsAndUpdatesService.addNewsAndUpdates(newsAndUpdatesDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/view-all")
    public ResponseEntity<?> getAll(){
        var response= newsAndUpdatesService.getAll();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}/editing")
    public ResponseEntity<?> updateNewsAndUpdates(@PathVariable Long id, @ModelAttribute NewsAndUpdatesDto newsAndUpdatesDto) {

        var response = newsAndUpdatesService.updateNewsAndUpdates(id, newsAndUpdatesDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}/deleting")
    public ResponseEntity<?> deleteNewsAndUpdatesById(@PathVariable Long id) {
        var response= newsAndUpdatesService.deleteNewsAndUpdatesById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
