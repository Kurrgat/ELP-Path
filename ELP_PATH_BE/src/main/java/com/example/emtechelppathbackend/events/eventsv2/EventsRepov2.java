package com.example.emtechelppathbackend.events.eventsv2;

import com.example.emtechelppathbackend.events.EventStatus;
import com.example.emtechelppathbackend.utils.EventApprovalStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRepov2 extends JpaRepository<EventsV2, Long> {
    @Query(value = "SELECT * FROM eventv2_details WHERE chapter_id = :chapterId AND approval_status = 'APPROVED' ORDER BY id DESC", nativeQuery = true)
    List<EventsV2> findEventsByChapterId(Long chapterId);

    @Query(value = "SELECT * FROM eventv2_details WHERE hub_id = :hubId AND approval_status = 'APPROVED' ORDER BY id DESC", nativeQuery = true)
    List<EventsV2> findEventsByHubId(Long hubId);
    @Query(value = "SELECT e.id AS eventId,e.approval_type AS approvalType, \n" +
            "u.first_name AS firstName, u.last_name AS lastName,\n" +
            " e.event_name AS eventName, e.event_status AS eventStatus,\n" +
            "  e.event_date AS eventDate, e.organizer AS organizer,\n" +
            "   e.chapter_id AS chapterId, e.hub_id AS hubId,\n" +
            "\t e.record_date AS recordDate,\n" +
            " c.nick_name AS chapterName\n" +
            " , h.hub_name AS hubName \n" +
            "            FROM eventv2_details e\n" +
            "            LEFT JOIN user_details u ON e.user_id = u.id\n" +
            "            LEFT JOIN chapterv2 c ON e.chapter_id = c.id\n" +
            "            LEFT JOIN hubsv2 h ON e.hub_id = h.id\n" +
            "             WHERE approval_status = 'PENDING' AND e.event_date > NOW()", nativeQuery = true)
    List<ApprovalStatusInterface> findByEventApprovalStatus(EventApprovalStatus eventApprovalStatus);



    @Query(value = "SELECT * FROM  eventv2_details e WHERE e.event_status = 'PAST'",nativeQuery = true)
    List<EventsV2> findByEventStatus(EventStatusv2 eventStatusv2);




    @Query(value = "SELECT * FROM  eventv2_details e WHERE e.event_date = :currentDate",nativeQuery = true)
    List<EventsV2> findByEventDate(LocalDateTime currentDate);




    @Query(value = "SELECT * FROM eventv2_details e WHERE e.event_name = :eventName",nativeQuery = true)
    Optional<EventsV2> findByEventName(String eventName);
    @Query(value = "SELECT e.approval_status AS status, \n" +
            "         e.approval_type AS approvalType,\n" +
            "            e.event_name AS eventName,\n" +
            "             c.nick_name AS chapterName,\n" +
            "              e.organizer AS organizer\n" +
            "             \n" +
            "                       FROM  eventv2_details e\n" +
            "                      LEFT JOIN chapterv2 c ON e.chapter_id = c.id\n" +
            "                      WHERE e.chapter_id = :chapterId  AND  e.approval_status = 'PENDING' AND e.event_date > NOW()",nativeQuery = true)
    List<ChapterEventsApprovalView> findByEventApprovalStatusAndChapterId(Long chapterId);

    @Query(value = "SELECT e.approval_status AS status, e.approval_type AS approvalType,\n" +
            " e.event_name AS eventName,\n" +
            "  h.hub_name AS hubName,\n" +
            "   e.organizer AS organizer\n" +
            "             FROM  eventv2_details e \n" +
            "            LEFT JOIN hubsv2 h ON e.hub_id = h.id\n" +
            "            WHERE e.hub_id = :hubId AND e.approval_status = 'PENDING' AND e.event_date > NOW()",nativeQuery = true)
    List<ChapterEventsApprovalView> findByEventApprovalStatusAndHubId(Long hubId);

    interface ChapterEventsApprovalView{
        String getStatus();
        String getApprovalType();
        String getEventName();
        String getChapterName();
        String getHubName();
        String getOrganizer();
    }
    interface ApprovalStatusInterface{
        Long getEventId();
        String getFirstName();
        String getLastName();
        String getEventName();
        String getEventStatus();
        LocalDate getEventDate();
        String getOrganizer();
        Long getChapterId();
        Long getHubId();
        LocalDateTime getRecordDate();
        String getChapterName();
        String getHubName();
        String getApprovalType();


    }
    @Query(value = "SELECT * FROM eventv2_details WHERE approval_status IS NULL OR approval_status = 'APPROVED' ORDER BY id DESC", nativeQuery = true)
    List<EventsV2> findAllEvents();

    interface  ChapterReportInterface{
    Integer getChapterId();
    String getChapterType();
    String getChapterName();
    String getChapterLeaderName();
    Integer getActiveMembers();
    Integer getDormantMembers();
    Integer getTotalEvents();
}
    @Query(value = "SELECT c.id AS chapterId,\n" +
        "       c.chapter_type AS chapterType, \n" +
        "       c.nick_name AS chapterName,\n" +
        "       s.scholar_first_name AS chapterLeaderName,\n" +
        "       COUNT(cm.user_id) AS activeMembers,\n" +
        "       COUNT(cm.date_left !=NULL) AS dormantMembers,\n" +
        "       COUNT(e.id) AS totalEvents\n" +
        "FROM chapterv2 c\n" +
        "LEFT JOIN chapter_memberv2 cm ON c.region_or_institution_id = cm.chapterv2_id\n" +
        "LEFT JOIN eventv2_details e ON c.region_or_institution_id = e.chapter_id\n" +
        "LEFT JOIN scholar s ON c.chapter_leader_id = s.id\n" +
        "WHERE cm.active_membership = true\n" +
        "GROUP BY c.id, c.chapter_type, c.nick_name, s.name;",nativeQuery = true)
    Collection<ChapterReportInterface> findChaptersReports();

    interface EventInterface{
        Integer getEventId();
        String getEventDate();
        String getEventDescription();
        String getEventName();
        String getLocation();
        String getOrganizer();
        Integer getActiveParticipatorsCount();
    }
    @Query(value = "SELECT \n" +
            "    e.id AS eventId,\n" +
            "    e.event_date AS eventDate,\n" +
            "    e.event_description AS eventDescription,\n" +
            "    e.event_name AS eventName,\n" +
            "    e.location AS location,\n" +
            "    e.organizer AS organizer,\n" +
            "    COUNT(ep.user_id) AS activeParticipatorsCount\n" +
            "FROM \n" +
            "    eventv2_details e\n" +
            "LEFT JOIN \n" +
            "    events_participatorsv2 ep ON e.id = ep.event_id AND ep.active_participation = TRUE\n" +
            "WHERE \n" +
            "    e.event_date >= :startDate AND e.event_date <= :endDate\n" +
            "GROUP BY \n" +
            "    e.id, e.event_date, e.event_description, e.event_name, e.location, e.organizer",nativeQuery = true)
    Collection<EventInterface> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
