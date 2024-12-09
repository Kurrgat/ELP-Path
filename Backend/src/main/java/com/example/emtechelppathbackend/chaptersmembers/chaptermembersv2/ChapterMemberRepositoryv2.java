package com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2;

import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChapterMemberRepositoryv2 extends JpaRepository<ChapterMemberV2, Long> {
	  List<ChapterMemberV2> findByChapterAndMember(ChapterV2 chapter, Users member);

	  Set<ChapterMemberV2> findByChapter_IdAndActiveMembershipIsTrue(Long chapterId);
}
