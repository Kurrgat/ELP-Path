package com.example.emtechelppathbackend.hubmembers;

import com.example.emtechelppathbackend.security.user.Users;

import java.util.List;
import java.util.Set;

public interface HubMemberService {
    Set<Users> extractUsersFromHubMembers(Set<HubMember> hubMembers);
    HubMember getActiveMembership(List<HubMember> memberships);
}
