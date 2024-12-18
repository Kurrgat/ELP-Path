package com.example.emtechelppathbackend.chapter;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chapters")
public class ChapterController {
    private final ChapterService chapterService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createChapter(
            @ModelAttribute ChapterDto chapterDto, @RequestParam(value = "file") MultipartFile file) {
        try {
            Chapter chapter = modelMapper.map(chapterDto, Chapter.class);
            chapterService.createChapter(chapter, file);
            return ResponseEntity.ok(new ResponseRecordOFMessages("chapter created successfully", null));
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @GetMapping("/v2/{chapterId}/view")
    public ResponseEntity<?> getChapterById(@PathVariable(value = "chapterId") Long chapterId, @RequestParam boolean condition) {
        var result = chapterService.getChapterById(chapterId, condition);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @GetMapping("/{chapterId}/display-chapter-members")
    public ResponseEntity<?> getChapterMembers(@PathVariable Long chapterId) {

            CustomResponse<Set<UsersDto>> members = chapterService.getMembersByChapterId(chapterId);
           return ResponseEntity.status(members.getStatusCode()).body(members);

    }

    @GetMapping("/all")
    ResponseEntity<CustomResponse<List<ChapterDto>>> getAllChapters(@RequestParam boolean condition) {

        var response=chapterService.getAllChapters(condition);
      return   ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/v2/chapter-scholars/{chapterId}")
    ResponseEntity<?> getChapterScholars(@PathVariable Long chapterId, @RequestParam boolean condition) {
        var response = chapterService.getChapterScholars(chapterId, condition);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/v2/institution-chapter/category")
    ResponseEntity<?> getInstitutionChapterByCategory(@RequestParam String institutionCategory, @RequestParam boolean condition) {
        var result = chapterService.getInstitutionChapterByCategory(institutionCategory, condition);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @GetMapping("/v2/institutional-region")
    ResponseEntity<?> getInstitutionOrRegionChapter(@RequestParam String chapterType, @RequestParam boolean condition) {
        var result = chapterService.getInstitutionOrRegionalChapter(chapterType, condition);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PutMapping("/v2/set-leader/{chapterId}/{userId}")
    ResponseEntity<?> setChapterLeader(@PathVariable Long chapterId, @PathVariable Long userId) {
        var result = chapterService.setChapterLeader(chapterId, userId, false);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }


    @PutMapping("/{chapterId}/update")
    public ResponseEntity<?> updateChapter(@PathVariable(value = "chapterId") Long chapterId, @ModelAttribute ChapterDTOUpdate chapterDto,
                                           @RequestParam(value = "chapterImage", required = false) MultipartFile chapterImage) {
        try {
            Chapter updatedChapter = chapterService.updateChapterByChapterId(chapterId, chapterDto, chapterImage);
            ChapterDto response = modelMapper.map(updatedChapter, ChapterDto.class);
            return ResponseEntity.ok(new ResponseRecordOFMessages("update successful", null));
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages("error when processing image", e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }


    @PostMapping("/{userId}/{chapterId}/join")
    public ResponseEntity<?> joinChapter(@PathVariable(value = "userId") Long userId, @PathVariable(value = "chapterId") Long chapterId) {
        try {
            Chapter joinedChapter = chapterService.joinChapter(userId, chapterId);
            return new ResponseEntity<>(joinedChapter, HttpStatus.CREATED);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/{chapterId}/leave")
    public ResponseEntity<?> leaveChapter(@PathVariable(value = "userId") Long userId, @PathVariable(value = "chapterId") Long chapterId) {
        try {

            Chapter leftChapter = chapterService.leaveChapter(userId, chapterId);
            return new ResponseEntity<>(leftChapter, HttpStatus.OK);
        }catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{chapterId}/delete")
    public ResponseEntity<?> deleteChapter(@PathVariable(value = "chapterId") Long chapterId) {
        chapterService.deleteChapter(chapterId);
        return new ResponseEntity<>("chapter deleted", HttpStatus.OK);
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

    @GetMapping("v2/all")
    public ResponseEntity<?> fetchAllChapters() {
        var response = chapterService.fetchAllChapterV2();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
