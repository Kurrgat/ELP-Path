package com.example.emtechelppathbackend.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    @Query(value = "SELECT * FROM like_entity  WHERE feed_id =:feedId", nativeQuery = true)
    List<LikeEntity> findCommentsByFeedId(Long feedId);

    interface LikeCount {
        Long getCount();
    }

    @Query(value = "SELECT COUNT(*) as count FROM like_entity  WHERE feed_id =:feedId", nativeQuery = true)
    LikeCount countByFeedId(Long feedId);
}
