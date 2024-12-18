package com.example.emtechelppathbackend.hubmembers.hubmembersv2;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HubMemberServiceImplv2 implements HubMemberServicev2 {

    @Override
    public List<Users> extractUsersFromHubMembers(List<HubMemberv2> hubMembers) {
        return hubMembers.stream()
                .map(HubMemberv2::getMember)
                .collect(Collectors.toList());
    }

    @Override
    public HubMemberv2 getActiveMembership(List<HubMemberv2> memberships) {
        for (HubMemberv2 membership : memberships) {
            if (membership.isActiveMembership()) {
                return membership;
            }
        }
        return null;
    }



}
