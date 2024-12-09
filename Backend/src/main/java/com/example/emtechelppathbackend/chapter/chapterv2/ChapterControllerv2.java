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
                                           @RequestParam(value = "chapterImage", required = false) MultipartFile chapterImage) {
        try {
            var response = chapterService.updateChapterByChapterId(chapterId, chapterDto, chapterImage);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error when processing image", e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @PostMapping("/{userId}/{chapterId}/join")
    public ResponseEntity<?> joinChapter(@PathVariable(value = "userId") Long userId, @PathVariable(value = "chapterId") Long chapterId) {
        try {
            var joinedChapter = chapterService.joinChapter(userId, chapterId);
            return ResponseEntity.status(joinedChapter.getStatusCode()).body(joinedChapter);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/{chapterId}/leave")
    public ResponseEntity<?> leaveChapter(@PathVariable(value = "userId") Long userId, @PathVariable(value = "chapterId") Long chapterId) {
        try {

            var leftChapter = chapterService.leaveChapter(userId, chapterId);
            return ResponseEntity.status(leftChapter.getStatusCode()).body(leftChapter);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/{scholarId}/chapters")
    public ResponseEntity<?> fetchChaptersThatScholarBelongsTo(@PathVariable Long scholarId) {
        var response = chapterService.fetchChaptersThatScholarBelongsTo(scholarId);
        return ResponseEntity.status(response.getStatusCode()).body(response
        );
    }
}
