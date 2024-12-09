package com.example.emtechelppathbackend.feed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedsRepository extends JpaRepository<Feeds, Long> {




    List<Feeds> findAllByUserId(Long user_id) ;

    @Override
    @Query(value = """
            SELECT * FROM feeds order by post_date desc limit 10;
            """,nativeQuery = true)
    List<Feeds> findAll();

}
