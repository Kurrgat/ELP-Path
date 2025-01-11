package com.example.emtechelppathbackend.chaptersmembers;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChapterMemberServiceImp implements ChapterMemberService {
    @Override
    public Set<Users> extractUsersFromChapterMembers(Set<ChapterMember> chapterMembers) {
        return chapterMembers.stream()
                .map(ChapterMember::getMember)
                .collect(Collectors.toSet());
    }

    @Override
    public ChapterMember getActiveMembership(List<ChapterMember> memberships) {
        for (ChapterMember membership : memberships) {
            if (membership.isActiveMembership()) {
                return membership;
            }
        }
        return null;
    }
}
