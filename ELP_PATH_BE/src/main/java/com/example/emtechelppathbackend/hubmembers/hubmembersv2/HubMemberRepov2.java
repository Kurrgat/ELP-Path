package com.example.emtechelppathbackend.hubmembers.hubmembersv2;

import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HubMemberRepov2 extends JpaRepository<HubMemberv2, Long> {
    List<HubMemberv2> findByHubAndMember(Hubv2 hub, Users member);
    @Query(value = "SELECT * FROM hub_memberv2 WHERE hub_id = :hubId AND active_membership = 1",nativeQuery = true)
    List<HubMemberv2> findByHubIdAndActiveMembershipIsTrue(Long hubId);

    @Query(value = "SELECT u.first_name AS firstName, u.last_name AS lastName, u.user_email AS email,u.username AS username, h.hub_name AS hubName, hm.id AS id, hm.date_joined AS requestDate\n" +
            " FROM hub_memberv2  hm\n" +
            "LEFT JOIN user_details u ON  hm.user_id=u.id\n" +
            "LEFT JOIN hubsv2 h ON hm.hub_id = h.id\n" +
            "WHERE status = 'APPROVED' AND h.id= :hubId",nativeQuery = true)
    List<HubMemberInterface1> findByHubIdAndActiveMembershipIsTrue1(Long hubId);
    @Query(value = "SELECT * FROM hub_memberv2 hm WHERE hm.user_id = :userId AND hm.`status`= 'APPROVED' ", nativeQuery = true)
    List<HubMemberv2> findMembershipByUserId( Long userId);
    @Query(value = "SELECT u.first_name AS firstName, u.last_name AS lastName, u.user_email AS email, h.hub_name AS hubName, hm.id AS id, hm.date_joined AS requestDate\n" +
            " FROM hub_memberv2  hm\n" +
            "LEFT JOIN user_details u ON  hm.user_id=u.id\n" +
            "LEFT JOIN hubsv2 h ON hm.hub_id = h.id\n" +
            "WHERE status = 'PENDING'", nativeQuery = true)
    List<HubMemberInterface1> findByStatus(MembershipStatus membershipStatus);

    interface  HubMemberInterface1{

        String getFirstName ();
        String getLastName ();
        String getEmail ();
        String getHubName ();
        Long getId();
        LocalDate getRequestDate();
    }



    @Query(value = "SELECT * FROM hub_memberv2 hm WHERE hm.hub_id = :id AND hm.active_membership = :status", nativeQuery = true)
    List<HubMemberv2> findAllByIdAndStatus(Long id,  MembershipStatus status);

    List<HubMemberv2> findByHubAndMemberAndStatus(Hubv2 hub, Users user, MembershipStatus membershipStatus);

    List<HubMemberv2> findByHubId(Long hubId);
@Query(value = "SELECT * from  hub_memberv2 hm WHERE hm.hub_id = :hubId AND hm.user_id = :userId AND hm.active_membership=true",nativeQuery = true)
    Optional<HubMemberv2> findByHubIdAndUserId(Long hubId, Long userId);



    @Query(value = "SELECT u.id AS userId, hm.active_membership AS activeMembership, hm.hub_id AS hubId from  user_details u \n" +
            "LEFT JOIN hub_memberv2 hm ON u.id =hm.user_id\n" +
            " WHERE u.id = hm.user_id AND hm.status= 'APPROVED' AND hm.hub_id = :id",nativeQuery = true)
    List<HubMembership> findHubMembersByHubId(Long id);
    @Query(value = "SELECT * from  hub_memberv2 hm WHERE  hm.user_id = :memberId AND hm.active_membership= :membershipStatus",nativeQuery = true)
    Optional<HubMemberv2> findByIdAndStatus(Long memberId, MembershipStatus membershipStatus);

    interface HubMembership{
        Long getUserId();
        Boolean getActiveMembership();
        Long getHubId();
    }


    interface HubMemberInterface{
    Long getUserId();
    String getFirstName();
    String getLastName();
    String getJoiningDate();
    Boolean getActiveMembership();
}
@Query(value = "SELECT \n" +
        "       u.id AS userId,\n" +
        "       u.first_name AS firstName,\n" +
        "       u.last_name AS lastName,\n" +
        "       hm.date_joined AS joiningDate,\n" +
        "       hm.active_membership AS activeMembership\n" +
        "FROM scholar s\n" +
        "LEFT JOIN hub_memberv2 hm ON s.id = hm.user_id\n" +
        "LEFT JOIN user_details u ON u.id = s.id\n" +
        "WHERE hm.hub_id = :hubId \n ",nativeQuery = true)
    List<HubMemberInterface> getHubMembersByHubId(Long hubId);
}
