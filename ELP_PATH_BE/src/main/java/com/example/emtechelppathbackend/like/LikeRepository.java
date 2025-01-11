package com.example.emtechelppathbackend.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    @Query(value = "SELECT l.id AS id,l.user_id AS userId, l.feed_id AS feedId, u.first_name AS firstname, u.last_name AS lastname\n" +
            "FROM like_entity l\n" +
            "LEFT JOIN user_details u ON l.user_id = u.id\n" +
            "WHERE l.feed_id = :feedId", nativeQuery = true)
    List<LikeInterface> findLikesByFeedId(Long feedId);

    void deleteByFeedId(Long id);
    @Query(value = "DELETE FROM like_entity WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(Long userId);

    interface LikeCount {
        Long getCount();
    }
    interface LikeInterface {
        Long getId();
        Long getFeedId();
        Long getUserId();
        String getFirstname();
        String getLastname();
    }



    @Query(value = "SELECT COUNT(*) as count FROM like_entity  WHERE feed_id =:feedId", nativeQuery = true)
    LikeCount countByFeedId(Long feedId);
}
