package com.example.emtechelppathbackend.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepo extends JpaRepository<RewardEntity, Long> {

}
