package com.example.emtechelppathbackend.comment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query(value = "SELECT * FROM comment_entity  WHERE feed_id =:feedId", nativeQuery = true)

    List<CommentEntity> findCommentsByFeedId(Long feedId);

    interface CommentCount {
        Integer getCount();
    }
    @Query(value = "SELECT COUNT(*) as count FROM comment_entity  WHERE feed_id =:feedId", nativeQuery = true)
    CommentCount countCommentsByFeedId(@Param("feedId") Long feedId);

}
