package com.example.emtechelppathbackend.chapter.chapterv2;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdatesDto;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/chapters")
public class ChapterControllerv2 {
    private final ChapterServicev2 chapterService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createChapter(@ModelAttribute ChapterDtov2 chapterDtov2) throws IOException {
            var response = chapterService.createChapter(chapterDtov2);
            return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/{chapterId}/view")
    public ResponseEntity<?> getChapterById(@PathVariable(value = "chapterId") Long chapterId) {
        var result = chapterService.getChapterById(chapterId);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @GetMapping("/{chapterId}/display-chapter-members")
    public ResponseEntity<?> getChapterMembers(@PathVariable Long chapterId) {
        try {
            var members = chapterService.getMembersByChapterId(chapterId);
            return ResponseEntity.status(members.getStatusCode()).body(members);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllChapters() {
        var chapters = chapterService.getAllChapters();
        return ResponseEntity.status(chapters.getStatusCode()).body(chapters);
    }

    @PutMapping("/{chapterId}/update")
    public ResponseEntity<?> updateChapter(@PathVariable(value = "chapterId") Long chapterId, @ModelAttribute ChapterDTOUpdatev2 chapterDto,
                                           @RequestParam(value = "chapterImage", required = false) MultipartFile chapterImage) throws IOException {

            var response = chapterService.updateChapterByChapterId(chapterId, chapterDto, chapterImage);
            return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @PostMapping("/{userId}/join")
    public ResponseEntity<?> joinChapter(@PathVariable(value = "userId") Long userId) {

            var joinedChapter = chapterService.joinChapter(userId);
            return ResponseEntity.status(joinedChapter.getStatusCode()).body(joinedChapter);

    }
    @PutMapping("/v2/set-leader/{chapterId}/{userId}")
    ResponseEntity<?> setChapterLeader(@PathVariable Long chapterId, @PathVariable Long userId) {
        var result = chapterService.setChapterLeader(chapterId, userId, false);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/{userId}/{chapterId}/leave")
    public ResponseEntity<?> leaveChapter(@PathVariable(value = "userId") Long userId, @PathVariable(value = "chapterId") Long chapterId) {
            var leftChapter = chapterService.leaveChapter(userId, chapterId);
            return ResponseEntity.status(leftChapter.getStatusCode()).body(leftChapter);
    }

    @DeleteMapping("/{chapterId}/delete")
    public ResponseEntity<?> deleteChapter(@PathVariable(value = "chapterId") Long chapterId) {
        var response = chapterService.deleteChapter(chapterId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/handlers/all")
    public ResponseEntity<?> fetchChapterHandlers(){
        var response = chapterService.fetchChapterHandlers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{userId}/chapters")
    public ResponseEntity<?> fetchChaptersThatScholarBelongsTo(@PathVariable Long userId) {
        var response = chapterService.fetchChaptersThatScholarBelongsTo(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-chapters-with-members")
    public ResponseEntity<?> getChaptersWithMembers(){
        var response = chapterService.getChaptersWithMembers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

