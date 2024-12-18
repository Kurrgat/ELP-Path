package com.example.emtechelppathbackend.hubs;

import com.querydsl.core.Fetchable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HubsRepo extends JpaRepository<Hub,Long> {

    interface HubReportInterface{
         Integer getHubId();
         String getHubName();
         String getHubAdmin();
         Integer getActiveMembers();
         Integer getInactiveMembers();
         Integer getTotalMembers();
    }
@Query(value = "SELECT \n" +
        "    h.id AS hubId,\n" +
        "    h.hub_name AS hubName,\n" +
        "    CONCAT(s.scholar_first_name, ' ', s.scholar_last_name) AS hubAdmin,\n" +
        "    COUNT(CASE WHEN hm.active_membership = TRUE THEN 1 END) AS activeMembers,\n" +
        "    COUNT(CASE WHEN hm.status = 'INACTIVE' THEN 1 END) AS inactiveMembers,\n" +
        "    COUNT(hm.id) AS totalMembers\n" +
        "FROM \n" +
        "    Hubsv2 h\n" +
        "LEFT JOIN \n" +
        "    Scholar s ON h.hub_admin_id = s.id\n" +
        "LEFT JOIN \n" +
        "    hub_memberv2 hm ON h.id = hm.hub_id\n" +
        "WHERE \n" +
        "    h.hub_admin_id IS NOT NULL\n" +
        "GROUP BY \n" +
        "    h.id, s.scholar_first_name, s.scholar_last_name",nativeQuery = true)
    List<HubReportInterface> findHubsReports();
}
