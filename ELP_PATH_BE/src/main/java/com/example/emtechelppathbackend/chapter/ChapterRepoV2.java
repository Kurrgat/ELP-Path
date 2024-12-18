package com.example.emtechelppathbackend.chapter;

import java.util.List;
import java.util.Optional;

import com.example.emtechelppathbackend.education.Institution;
import com.example.emtechelppathbackend.utils.KenyaCounty;
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

    @Query(value = "SELECT * " +
            "FROM chapterv2 c" +
            " WHERE c.region_or_institution_id = :institutionId", nativeQuery = true)
    ChapterV2 findByInstitution(Long institutionId);



    @Query(value = "SELECT * " +
            "FROM chapterv2 c" +
            " WHERE c.region_or_institution_id = :id", nativeQuery = true)
    List<ChapterV2> findByKenyanCounty(Long id);
    @Query(value = "SELECT c.* \n" +
            "FROM chapterv2 c  \n" +
            "LEFT JOIN chapter_memberv2 cm ON c.id = cm.chapterv2_id \n" +
            "WHERE c.id = :chapterId\n" +
            "AND (:includeCondition = true OR (cm.active_membership = true AND cm.date_left IS NULL)) \n" +
            "GROUP BY c.id", nativeQuery = true)
    Optional<ChapterV2> getChaptersById(Long chapterId, boolean includeCondition);

@Query(value = "SELECT DISTINCT c.* " +
        "FROM chapter_memberv2 cm \n" +
        "left JOIN chapterv2 c ON cm.chapterv2_id = c.id \n" +
        "WHERE cm.active_membership = TRUE",nativeQuery = true)
    List<ChapterV2> findChaptersWithActiveMembers();

@Query(value = "SELECT * FROM chapterv2 c WHERE c.region_or_institution_id = :institutionId", nativeQuery = true)
    Optional<ChapterV2> findByInstitutionId(Long institutionId);
//@Query(value = "select * from chapterv2 where id = :chapterId", nativeQuery = true)
//    Optional<ChapterV2> getByChapterId(Long chapterId);
//
//@Query(value = "select * from chapterv2 WHERE nick_name LIKE 'Global Universities%'")
//    Optional<ChapterV2> findByDescription();
}
