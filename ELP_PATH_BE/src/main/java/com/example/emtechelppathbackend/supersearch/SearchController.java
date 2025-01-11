package com.example.emtechelppathbackend.supersearch;

import com.example.emtechelppathbackend.supersearch.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<?> searchEntities(@RequestParam("q") String keyword) {
        var searchResults = searchService.searchEntities(keyword);
        return ResponseEntity.status(searchResults.getStatusCode()).body(searchResults);
    }
}
