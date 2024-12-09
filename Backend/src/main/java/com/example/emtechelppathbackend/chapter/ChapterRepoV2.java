package com.example.emtechelppathbackend.chapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.emtechelppathbackend.scholars.Scholar;

@Repository
public interface ChapterRepoV2 extends JpaRepository<ChapterV2, Long> {

    @Query(value = "SELECT c.* " +
                   "FROM scholar s " +
                   "LEFT JOIN institution i ON s.scholar_tertiary_institution = i.id " +
                   "LEFT JOIN chapterv2 c ON s.scholar_tertiary_institution = c.region_or_institution_id " +
                   "LEFT JOIN user_details u ON s.id = u.scholar_id " +
                   "WHERE s.scholar_tertiary_institution IS NOT NULL AND (:includeCondition = true OR u.scholar_id IS NOT NULL)", nativeQuery = true)
    List<ChapterV2> getChapters(@Param("includeCondition") boolean includeCondition);

    @Query(value = "SELECT c.* " +
                "FROM scholar s " +
                "LEFT JOIN institution i ON s.scholar_tertiary_institution = i.id " +
                "LEFT JOIN chapterv2 c ON s.scholar_tertiary_institution = c.region_or_institution_id " +
                "LEFT JOIN user_details u ON s.id = u.scholar_id " +
                "WHERE s.scholar_tertiary_institution IS NOT NULL AND c.id = :chapterId AND (:includeCondition = true OR u.scholar_id IS NOT NULL)", nativeQuery = true)
    Optional<ChapterV2> getChapterById(@Param("chapterId") Long chapterId, @Param("includeCondition") boolean includeCondition);

    @Query(value = "SELECT s.*" +
            "FROM scholar s " +
            "LEFT JOIN institution i ON s.scholar_tertiary_institution = i.id " +
            "LEFT JOIN chapterv2 c ON s.scholar_tertiary_institution = c.region_or_institution_id " +
            "LEFT JOIN user_details u ON s.id = u.scholar_id " +
            "WHERE s.scholar_tertiary_institution IS NOT NULL AND c.id = :chapterId AND (:includeCondition = true OR u.scholar_id IS NOT NULL)", nativeQuery = true)
    List<Scholar> getChapterScholars(@Param("chapterId") Long chapterId, @Param("includeCondition") boolean includeCondition);
    
    @Query(value = "SELECT c.*" +
                "FROM scholar s " +
                "LEFT JOIN institution i ON s.scholar_tertiary_institution = i.id " +
                "LEFT JOIN chapterv2 c ON s.scholar_tertiary_institution = c.region_or_institution_id " +
                "LEFT JOIN user_details u ON s.id = u.scholar_id " +
                "WHERE s.scholar_tertiary_institution IS NOT NULL AND i.category = :institutionCategory AND (:includeCondition = true OR u.scholar_id IS NOT NULL)", nativeQuery = true)
    List<ChapterV2> getInstitutionChapterByCategory(@Param("institutionCategory") String institutionCategory, @Param("includeCondition") boolean includeCondition);

    @Query(value = "SELECT c.*" +
            "FROM scholar s " +
            "LEFT JOIN institution i ON s.scholar_tertiary_institution = i.id " +
            "LEFT JOIN chapterv2 c ON s.scholar_tertiary_institution = c.region_or_institution_id " +
            "LEFT JOIN user_details u ON s.id = u.scholar_id " +
            "WHERE s.scholar_tertiary_institution IS NOT NULL AND c.chapter_type = :chapterType AND (:includeCondition = true OR u.scholar_id IS NOT NULL)", nativeQuery = true)
    List<ChapterV2> getInstitutionOrRegionalChapter(@Param("chapterType") String chapterType, @Param("includeCondition") boolean includeCondition);   
}
