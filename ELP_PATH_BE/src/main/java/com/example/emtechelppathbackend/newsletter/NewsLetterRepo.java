package com.example.emtechelppathbackend.newsletter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLetterRepo extends JpaRepository<NewsLetter, Long> {
    boolean existsByEmail(String email);
}
