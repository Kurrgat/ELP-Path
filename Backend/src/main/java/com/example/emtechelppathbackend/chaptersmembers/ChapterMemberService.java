package com.example.emtechelppathbackend.chaptersmembers;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ChapterMemberService {

	  Set<Users> extractUsersFromChapterMembers(Set<ChapterMember> chapterMembers);

	  ChapterMember getActiveMembership(List<ChapterMember> memberships);
}
