package com.example.emtechelppathbackend.hubmembers;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HubMemberServiceImpl implements HubMemberService {

    @Override
    public Set<Users> extractUsersFromHubMembers(Set<HubMember> hubMembers) {
        return hubMembers.stream()
                .map(HubMember::getMember)
                .collect(Collectors.toSet());
    }

    @Override
    public HubMember getActiveMembership(List<HubMember> memberships) {
        for (HubMember membership : memberships) {
            if (membership.isActiveMembership()) {
                return membership;
            }
        }
        return null;
    }

}
