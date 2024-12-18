package com.example.emtechelppathbackend.chapter;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public interface ChapterService {

    void createChapter(Chapter chapter, MultipartFile file) throws IOException;

    //updated by m
    CustomResponse<ChapterDto> getChapterById(Long chapterId, boolean includeCondition);
    CustomResponse<List<ChapterDto>> getAllChapters(boolean includeCondition);
    CustomResponse<List<Scholar>> getChapterScholars(Long chapterId, boolean includeCondition);
    CustomResponse<List<ChapterDto>> getInstitutionChapterByCategory(String institutionCategory, boolean includeCondition);
    CustomResponse<List<ChapterDto>> getInstitutionOrRegionalChapter(String chapterType, boolean includeCondition);
    CustomResponse<?> setChapterLeader(Long chapterId, Long userId, boolean includeCondition);


    Chapter joinChapter(Long userId, Long chapterId);
    Chapter updateChapterByChapterId(Long chapterId, ChapterDTOUpdate updatedChapterDto, MultipartFile chapterImage) throws NoResourceFoundException, IOException;
    Chapter leaveChapter(Long userId, Long chapterId);
    void deleteChapter(Long chapterId);
    CustomResponse<Set<UsersDto>> getMembersByChapterId(Long chapterId) throws NoResourceFoundException;

    CustomResponse<?> fetchChapterHandlers();
    CustomResponse<?> fetchChaptersThatScholarBelongsTo(Long scholarId);

    CustomResponse<?> fetchAllChapterV2();


}
