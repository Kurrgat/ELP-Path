package com.example.emtechelppathbackend.feed.feedv2;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedsRepositoryv2 extends JpaRepository<FeedsV2, Long> {
    List<FeedsV2> findAllByUserId(Long user_id) ;


    List<FeedsV2> findByUser(Users users);

    List<FeedsV2> findByHubId(Long hubId, Sort postDate);

    List<FeedsV2> findByChapterId(Long chapterId, Sort postDate);

    Optional<FeedsV2> findByIdAndUserId(Long id, Long userId);
}
