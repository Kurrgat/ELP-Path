package com.example.emtechelppathbackend.skills;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {

    @Query(value = "SELECT * FROM user_skills WHERE user_id = :userId", nativeQuery = true)
    List<Skills> findByUserId( Long userId);


}