package com.example.emtechelppathbackend.country;

import com.example.emtechelppathbackend.education.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country,Long> {

}
