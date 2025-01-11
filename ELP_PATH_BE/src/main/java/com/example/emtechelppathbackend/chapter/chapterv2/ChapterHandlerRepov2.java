package com.example.emtechelppathbackend.chapter.chapterv2;

import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterHandlerRepov2 extends JpaRepository<ChapterHandlerv2, Long> {
    @Query(value = """
            SELECT ch.id,ch.scholar_id AS ScholarId,
                CONCAT(s.scholar_first_name,' ',s.scholar_last_name) AS ScholarName,
                ch.institution_id AS InstitutionChapterId, i.name AS InstitutionChapterName,
                ch.home_county_id AS RegionalChapterId, c.name AS RegionalChapterName
            FROM chapter_handler AS ch\s
            JOIN institution AS i
            ON ch.institution_id = i.id
            JOIN kenyan_county AS c
            ON ch.home_county_id = c.id
            JOIN scholar AS s
            ON ch.scholar_id = s.id
            """,nativeQuery = true)
    List<ChapterHandlerInterfacev2> fetchChapters();

}
