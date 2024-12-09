package com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChapterMemberServiceImpv2 implements ChapterMemberServicev2 {
    @Override
    public Set<Users> extractUsersFromChapterMembers(Set<ChapterMemberV2> chapterMembers) {
        return chapterMembers.stream()
                .map(ChapterMemberV2::getMember)
                .collect(Collectors.toSet());
    }

    @Override
    public ChapterMemberV2 getActiveMembership(List<ChapterMemberV2> memberships) {
        for (ChapterMemberV2 membership : memberships) {
            if (membership.isActiveMembership()) {
                return membership;
            }
        }
        return null;
    }
}
