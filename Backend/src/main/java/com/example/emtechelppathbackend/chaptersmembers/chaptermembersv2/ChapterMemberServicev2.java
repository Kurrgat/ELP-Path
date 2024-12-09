package com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ChapterMemberServicev2 {

	  Set<Users> extractUsersFromChapterMembers(Set<ChapterMemberV2> chapterMembers);

	  ChapterMemberV2 getActiveMembership(List<ChapterMemberV2> memberships);
}
