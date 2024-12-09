package com.example.emtechelppathbackend.hubmembers.hubmembersv2;

import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HubMemberRepov2 extends JpaRepository<HubMemberv2, Long> {
    List<HubMemberv2> findByHubAndMember(Hubv2 hub, Users member);

    Set<HubMemberv2> findByHub_IdAndActiveMembershipIsTrue(Long hubId);

}
