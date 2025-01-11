package com.example.emtechelppathbackend.hubs.hubsv2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface HubsRepov2 extends JpaRepository<Hubv2,Long> {
    @Query(value = "SELECT h.*\n" +
            "            FROM hubsv2 h\n" +
            "            LEFT JOIN hub_memberv2 hm ON h.id = hm.hub_id\n" +
            "            WHERE h.id = :hubId\n" +
            "            AND (:includeCondition = true OR (hm.active_membership = true AND hm.date_left IS NULL))\n" +
            "            GROUP BY h.id", nativeQuery = true)
    Optional<Hubv2> getHubById(Long hubId,  boolean includeCondition);

}
