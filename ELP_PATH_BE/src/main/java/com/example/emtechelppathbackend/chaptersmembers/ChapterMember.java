package com.example.emtechelppathbackend.chaptersmembers;

import com.example.emtechelppathbackend.chapter.Chapter;
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
@Table(name = "chapter_member")
public class ChapterMember {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "chapter_id")
	  private Chapter chapter;

	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private Users member;

	  @Column(name = "date_joined")
	  private LocalDateTime joiningDate;

	  @Column(name = "date_left")
	  private LocalDateTime leavingDate;

	  private boolean activeMembership;
}
