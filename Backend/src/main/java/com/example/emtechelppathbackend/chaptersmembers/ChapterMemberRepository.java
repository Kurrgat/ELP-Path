package com.example.emtechelppathbackend.chaptersmembers;

import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChapterMemberRepository extends JpaRepository<ChapterMember, Long> {
	  List<ChapterMember> findByChapterAndMember(Chapter chapter, Users member);

	  Set<ChapterMember> findByChapter_IdAndActiveMembershipIsTrue(Long chapterId);
}
