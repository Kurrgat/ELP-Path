package com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2;

import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.education.Education;
import com.example.emtechelppathbackend.institutionchecker.InstitutionChecker;
import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "chapter_memberv2")
public class ChapterMemberV2 {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "chapterv2_id")
	  private ChapterV2 chapter;

	  @ManyToOne
	  @JoinColumn
	  private InstitutionChecker institutionChecker;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private Users member;

	  @Column(name = "date_joined")
	  private LocalDateTime joiningDate;

	  @Column(name = "date_left")
	  private LocalDateTime leavingDate;

      @Column(name = "active_membership")
	  private boolean activeMembership;

	@ManyToOne
	@JoinColumn(name = "education_id")
	private Education education;

	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;
}
