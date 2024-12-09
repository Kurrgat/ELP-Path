package com.example.emtechelppathbackend.supersearch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<SearchDto, Long> {

    @Query(value = """
            SELECT s.* FROM\s
                (SELECT CONCAT(first_name,' ',last_name) AS content, ud.id,'user' AS type FROM user_details AS ud JOIN roles r ON r.id=ud.user_roles WHERE r.role_name<>'SUPER_ADMIN')\s
                AS s WHERE s.content LIKE %:keyword%
            UNION
            SELECT CONCAT(f.title, ' ',f.description) AS content, f.id, 'feed' AS type FROM feedsv2 AS f WHERE f.id NOT IN\s
                ( SELECT id  FROM feedsv2 WHERE identity_hate <>0 OR insult <> 0 OR obscene <> 0 OR threat <> 0 OR toxic <> 0)\s
                AND (f.title LIKE %:keyword% OR f.description LIKE %:keyword%)
            UNION
            SELECT CONCAT(activity_description, ' ', activity_name) AS content, id, 'activity' AS type FROM activity WHERE activity_description LIKE %:keyword% OR activity_name LIKE %:keyword%
            UNION
            SELECT CONCAT(event_description, ' ', event_name) AS content, id, 'event' AS type FROM event_details WHERE event_description LIKE %:keyword% OR event_name LIKE %:keyword%
            UNION
            SELECT CONCAT(chapter_description, ' ', chapter_name) AS content, id, 'chapter' AS type FROM chapter WHERE chapter_description LIKE %:keyword% OR chapter_name LIKE %:keyword%
            UNION
            SELECT CONCAT(hub_description, ' ', hub_name) AS content, id, 'hub' AS type FROM hubs WHERE hub_description LIKE %:keyword% OR hub_name LIKE %:keyword%
            UNION
            SELECT CONCAT(website, ' ', name) AS content, id, 'institution' AS type FROM institution WHERE website LIKE %:keyword% OR name LIKE %:keyword%
            """, nativeQuery = true)
    List<SearchDto> searchEntities(String keyword);

}
