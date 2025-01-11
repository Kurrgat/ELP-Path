package com.example.emtechelppathbackend.newsandupdates.usernewsview;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsAndUpdatesViewRepo extends JpaRepository<NewsAndUpdatesView, Long> {
    @Query(value = "SELECT * FROM news_view WHERE  news_and_updates_id = :newsAndUpdateId And user_id = :userId",nativeQuery = true)
    NewsAndUpdatesView findByIdAndUserId(Long newsAndUpdateId, Long userId);
}
