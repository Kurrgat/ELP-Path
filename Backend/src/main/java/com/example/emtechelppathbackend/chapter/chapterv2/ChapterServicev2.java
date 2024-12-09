package com.example.emtechelppathbackend.chapter.chapterv2;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public interface ChapterServicev2 {

    CustomResponse<?> createChapter(ChapterDtov2 chapterDtov2) throws IOException;
    CustomResponse<ChapterDtov2> getChapterById(Long chapterId);
    CustomResponse<?> getAllChapters();
    CustomResponse<?> joinChapter(Long userId, Long chapterId);
    CustomResponse<ChapterDtov2> updateChapterByChapterId(Long chapterId, ChapterDTOUpdatev2 updatedChapterDto, MultipartFile chapterImage) throws NoResourceFoundException, IOException;
    CustomResponse<?> leaveChapter(Long userId, Long chapterId);
    CustomResponse<?> deleteChapter(Long chapterId);
    CustomResponse<Set<UsersDto>> getMembersByChapterId(Long chapterId) throws NoResourceFoundException;

    CustomResponse<?> fetchChapterHandlers();
    CustomResponse<?> fetchChaptersThatScholarBelongsTo(Long scholarId);
}
