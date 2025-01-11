package com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2;

import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.education.Education;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChapterMemberRepositoryv2 extends JpaRepository<ChapterMemberV2, Long> {
	  List<ChapterMemberV2> findByChapterAndMember(ChapterV2 chapter, Users member);

	  @Query(value = " SELECT u.id AS userId, u.first_name AS firstName, cm.chapterv2_id AS chapterId FROM user_details u \n" +
			  " LEFT JOIN chapter_memberv2 cm ON cm.user_id = u.id\n" +
			  " WHERE cm.chapterv2_id = :chapterId AND cm.active_membership = TRUE",nativeQuery = true)
	  List<ChapterMembership> findByChapterMembers(Long chapterId);
@Query(value = "SELECT * FROM chapter_memberv2 cm WHERE cm.chapterv2_id = :chapterId AND cm.active_membership =TRUE",nativeQuery = true)
	List<ChapterMemberV2> findByChapterIdAndActiveMembershipIsTrue(Long chapterId);

	interface ChapterMembership{
		  Long getUserId();
		  String getFirstName();
		  Long getChapterId();

	  }


	@Query(value = "SELECT * FROM chapter_memberv2 WHERE user_id = :userId AND education_id = :educationId", nativeQuery = true)
	List<ChapterMemberV2> findByUserIdAndEducationId(Long userId, Long educationId);
	@Query(value = "SELECT * FROM chapter_memberv2 WHERE user_id = :userId", nativeQuery = true)
	List<ChapterMemberV2> findByUserId(Long userId);
	@Query(value = "SELECT * FROM chapter_memberv2 WHERE user_id = :userId AND profile_id = :id", nativeQuery = true)
	List<ChapterMemberV2> findByUserIdAndProfileId(Long userId, Long id);
	@Query(value = "SELECT * FROM chapter_memberv2 WHERE profile_id = :profileId", nativeQuery = true)
	List<ChapterMemberV2> findByProfileId(Long profileId);
@Query (value = "SELECT * from chapter_memberv2 WHERE education_id = :educationId", nativeQuery = true)
    List<ChapterMemberV2> findByEducationId(Long educationId);
	@Query (value = "SELECT * from chapter_memberv2 cm WHERE cm.chapterv2_id = :chapterId AND cm.user_id = :userId", nativeQuery = true)
    Optional<ChapterMemberV2> findByChapterIdAndUserId(Long chapterId, Long userId);


    interface ChapterScholarsInterface{
		Long getUserId();;
		String getFirstName();
		String getLastName();
		String getJoiningDate();
		Boolean getActiveMembership();


	}

	@Query(value = "SELECT \n" +
			"\t\tu.id AS userId,\n" +
			"\t\tu.first_name AS fastName,\n" +
			"\t\tu.last_name AS lastName,\n" +
			"\t\tcm.date_joined AS joiningDate,\n" +
			"\t\tcm.active_membership AS activeMembership\n" +
			"FROM user_details u\n" +
			"LEFT JOIN chapter_memberv2 cm ON u.id = cm.user_id\n" +
			"WHERE cm.chapterv2_id = 42 \n" +
			"AND cm.active_membership = TRUE\n"
			, nativeQuery = true)
	List<ChapterScholarsInterface> getChaptersScholars( Long chapterId);


	@Query(value = "SELECT\n" +
			"\t  c.chapter_leader_id AS chapterLeaderId,\n" +
			"\t  c.nick_name AS chapterName,\n" +
			"\t  c.chapter_type AS chapterType,\n" +
			"\t  c.description AS DESCRIPTION,\n" +
			"\t  c.chapter_image AS chapterImage,\n" +
			"\t  c.id AS chapterId,\n" +
			"\t  cm.active_membership AS activeMembership,\n" +
			"\t   CONCAT(s.scholar_first_name, ' ', s.scholar_last_name) AS chapterLeaderName\n" +
			"\t\t\tFROM chapter_memberv2 cm\n" +
			"\t\t\tLEFT JOIN user_details u ON cm.user_id = u.id\n" +
			"\t\t\tLEFT JOIN chapterv2 c ON cm.chapterv2_id = c.id\n" +
			"\t\t\tLEFT JOIN scholar s ON c.chapter_leader_id = s.id\n" +
			"\t\t\t\n" +
			"\t\t\tWHERE cm.user_id = :userId AND cm.active_membership =TRUE", nativeQuery = true)
	List<ChapterMemberInterface> fetchChapters(Long userId);


	interface ChapterMemberInterface{
		Long getChapterLeaderId();
		String getChapterName();
		String getChapterType();
		String getDescription();
		String getChapterImage();
		Long getChapterId();
		Boolean getActiveMembership();
		String getChapterLeaderName();
	}

}
