package com.example.emtechelppathbackend.newsletter;

import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdatesDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/NewsLetter")
@RequiredArgsConstructor
public class NewsLetterController {
    @Autowired
    private  final NewsLetterService newsLetterService;
    private final NewsLetterRepo newsLetterRepo;
    @PostMapping("/create")
    public ResponseEntity<?> addNewsLetter(@ModelAttribute NewsLetterDto newsLetterDto){

        var response = newsLetterService.addNewsLetter(newsLetterDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get-details/{id}")
    public ResponseEntity<?>findDetailsById(@PathVariable Long id){
        var response=newsLetterService.findDetailsById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/get-pdf/{id}")
    public ResponseEntity<Resource> findById(@PathVariable Long id) {

        HttpHeaders headers = new HttpHeaders();

        try {
            Optional<NewsLetter> newsLetterOptional = newsLetterRepo.findById(id);

            if (newsLetterOptional.isPresent()) {
                NewsLetter newsLetter = newsLetterOptional.get();

                String documentUrl = "images/" + newsLetter.getDocument();
                Path pdfPath = Paths.get(documentUrl);
                Resource pdf = new UrlResource(pdfPath.toUri());

                if (pdf.exists()) {
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    return ResponseEntity.ok().headers(headers).body(pdf);
                } else {
                    System.out.println("Document not found: " + documentUrl);
                    return ResponseEntity.notFound().build();
                }
            } else {
                System.out.println("Newsletter not found for id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}/deleting")
    public ResponseEntity<?> deleteNewsAndUpdatesById(@PathVariable Long id) {
        var response= newsLetterService.deleteNewsLetterById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateNewsLetter(@PathVariable Long id, @ModelAttribute NewsLetterDto newsLetterDto) {

        var response = newsLetterService.updateNewsLetter(id, newsLetterDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
