package com.example.emtechelppathbackend.comment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query(value = "SELECT c.id AS id, c.user_id AS userId, c.feed_id AS feedId, c.message AS message, u.first_name AS firstname, u.last_name AS lastname\n" +
            "FROM comment_entity c\n" +
            "LEFT JOIN user_details u ON c.user_id = u.id\n" +
            "WHERE c.feed_id = :feedId", nativeQuery = true)
    List< CommentInterface> findCommentsByFeedId(Long feedId);

    void deleteByFeedId(Long id);

    interface CommentInterface {
       Long getId();
       Long getFeedId();
       Long  getUserId();
       String getMessage();
       String getFirstname();
       String getLastname();
    }

    interface CommentCount {
        Integer getCount();
    }
    @Query(value = "SELECT COUNT(*) as count FROM comment_entity  WHERE feed_id =:feedId", nativeQuery = true)
    CommentCount countCommentsByFeedId(@Param("feedId") Long feedId);

}
