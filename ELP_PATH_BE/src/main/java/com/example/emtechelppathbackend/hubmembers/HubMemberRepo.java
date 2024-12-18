package com.example.emtechelppathbackend.hubmembers;

import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HubMemberRepo extends JpaRepository<HubMember, Long> {
    List<HubMember> findByHubAndMember(Hub hub, Users member);

    Set<HubMember> findByHub_IdAndActiveMembershipIsTrue(Long hubId);
}
