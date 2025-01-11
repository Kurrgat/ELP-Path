package com.example.emtechelppathbackend.hubmembers.hubmembersv2;

import com.example.emtechelppathbackend.security.user.Users;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HubMemberServicev2 {
    List<Users> extractUsersFromHubMembers(List<HubMemberv2> hubMembers);
    HubMemberv2 getActiveMembership(List<HubMemberv2> memberships);


}
