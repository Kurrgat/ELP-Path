package com.example.emtechelppathbackend.feed.feedv2;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedsRepositoryv2 extends JpaRepository<FeedsV2, Long> {
    List<FeedsV2> findAllByUserId(Long user_id) ;


    List<FeedsV2> findByUser(Users users);

    List<FeedsV2> findByHubId(Long hubId, Sort postDate);

    List<FeedsV2> findByChapterId(Long chapterId, Sort postDate);

    Optional<FeedsV2> findByIdAndUserId(Long id, Long userId);
@Query(value = "SELECT \n" +
        "    (SELECT COUNT(*) FROM like_entity WHERE feed_id = f.id) AS likesCount,\n" +
        "    (SELECT COUNT(*) FROM comment_entity WHERE feed_id =f.id) AS commentsCount, \n" +
        "   CONCAT ( u.first_name,' ',u.last_name) AS name,\n" +
        "    f.id AS feedId, \n" +
        "    f.user_id AS userId, \n" +
        "    f.description AS description, \n" +
        "    f.post_date AS postDate,\n" +
        "    c.id AS commentId, \n" +
        "    c.message AS comment, \n" +
        "    l.id AS likeId\n" +
        "FROM  \n" +
        "    feedsv2 f\n" +
        "LEFT JOIN \n" +
        "    comment_entity c ON f.id = c.feed_id\n" +
        "LEFT JOIN \n" +
        "    like_entity l ON f.id = l.feed_id\n" +
        "LEFT JOIN\n" +
        "\t\tuser_details u ON f.user_id = u.id\n" +
        "WHERE \n" +
        "    f.id = :feedId", nativeQuery = true)
    List<FeedsInterface> findFeedDetailsById(Long feedId);
@Query(value =
        "SELECT \n" +
        "         (SELECT COUNT(*) FROM like_entity WHERE feed_id = f.id) AS likesCount, \n" +
        "          (SELECT COUNT(*) FROM comment_entity WHERE feed_id = f.id) AS commentsCount, \n" +
        "           CONCAT(u.first_name, ' ', u.last_name) AS name, \n" +
        "           \n" +
        "           f.id AS feedId, \n" +
        "           f.user_id AS userId, \n" +
        "           f.description AS description, \n" +
        "           f.post_date AS postDate, \n" +
        "           c.id AS commentId, \n" +
        "           c.message AS comment, \n" +
        "           l.id AS likeId,\n" +
        "           fi.images AS feedImages\n" +
        "        FROM \n" +
        "           feedsv2 f \n" +
        "        LEFT JOIN \n" +
        "            comment_entity c ON f.id = c.feed_id \n" +
        "        LEFT JOIN \n" +
        "            like_entity l ON f.id = l.feed_id \n" +
        "        LEFT JOIN \n" +
        "           user_details u ON f.user_id = u.id \n" +
        "        LEFT JOIN \n" +
        "           feedv2_images fi ON f.id = fi.feedsv2_id \n" +
        "        ORDER BY f.post_date DESC",nativeQuery = true)
    List<FeedsInterface> getAllFeeds();

    interface  FeedsInterface{
     Long getFeedId();
     Long getLikesCount();
     Long getCommentsCount();
     String getComment();
     String getFeedImages();
     String getName();
     Long getUserId();
     String getDescription();
     LocalDateTime getPostDate();
     Long getCommentId();
     Long getLikeId();

 }
}
