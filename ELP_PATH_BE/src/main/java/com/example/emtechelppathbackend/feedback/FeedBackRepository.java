package com.example.emtechelppathbackend.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
