package com.example.emtechelppathbackend.Career;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRepo extends JpaRepository<Career,Long> {

    @Query(value = "SELECT * FROM career WHERE users_id = :userId ORDER BY id DESC", nativeQuery = true)
    List<Career> findByUserId( Long userId);



}
