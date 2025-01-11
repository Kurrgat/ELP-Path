package com.example.emtechelppathbackend.bio;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BioRepository extends JpaRepository<Bio,Long> {
    //retrieve the Bio if you have the userId
    Bio findBioByUserId(Long userId);
@Query(value = "SELECT * FROM bio WHERE users_id = :userId", nativeQuery = true)
    Optional<Bio> findByUserId(Long userId);
}
